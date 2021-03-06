(ns clj_talks.overtone
 (:use overtone.live
        overtone.inst.sampled-piano))

(demo (sin-osc))

;; sounds are generated by u-gens
(clojure.java.browse/browse-url "http://supercollider.sourceforge.net")

(defsynth thx [gate 1 amp 1 out-bus 0]
  (let [target-pitches (map midi->hz [77 74 72 70 65 62 60 58 53 50 46 34 26 22 14 10])
        r-freq         (env-gen:kr (envelope [1 1 0.007 10] [8 4 2] [0 -4 1] 2) gate)
        amp-env        (env-gen:kr (envelope [0 0.07 0.21 0] [8 4 2] [0 1 1] 2) gate :action FREE)
        mk-noise       (fn [ug-osc]
                         (mix (map #(pan2 (ug-osc (+ (* r-freq (+ 230 (* 100 (lf-noise2:kr 1.3))))
                                                     (env-gen:kr (envelope [0 0 %] [8 6] [0 -3]))))
                                          (lf-noise2:kr 5))
                                   target-pitches)))
        saws           (mk-noise saw)
        sins           (mk-noise sin-osc)
        snd            (+ (* saws amp-env) (* sins amp-env))]
    (out out-bus
         (* amp (g-verb snd 9 0.7 0)))))

(thx :amp 2)

;; (stop)

(examples)

(def piece [:E4 :F#4 :B4 :C#5 :D5 :F#4 :E4 :C#5 :B4 :F#4 :D5 :C#5])

(defn player
  [t speed notes]
  (let [n      (first notes)
        notes  (next notes)
        t-next (+ t speed)]
    (when n
      (at t
        (sampled-piano (note n)))
      (apply-by t-next #'player [t-next speed notes]))))

(def num-notes 1000)

(do
  (player (now) 338 (take num-notes (cycle piece)))
  (player (now) 335 (take num-notes (cycle piece))))

;; (stop)

;; another piano example:

(def chord-prog
  [#{[2 :minor7] [7 :minor7] [10 :major7]}
   #{[0 :minor7] [8 :major7]}])

(def beat-offsets [0 0.1 0.2 1/3  0.7 0.9])

(def metro (metronome 20))

(def root 40)
(def max-range 35)
(def range-variation 10)
(def range-period 8)

(defn beat-loop
  [metro beat chord-idx]
  (let [[tonic chord-name] (choose (seq (nth chord-prog chord-idx)))
        nxt-chord-idx      (mod (inc chord-idx) (count chord-prog))
        note-range         (cosr beat range-variation  max-range range-period)
        notes-to-play      (rand-chord (+ root tonic)
                                       chord-name
                                       (count beat-offsets)
                                       note-range)]
    (dorun
     (map (fn [note offset]
            (at (metro (+ beat offset)) (sampled-piano note 0.3)))
          notes-to-play
          beat-offsets))
    (apply-by (metro (inc beat)) #'beat-loop [metro (inc beat) nxt-chord-idx])))

(beat-loop metro (metro) 0)

;; (stop)

(clojure.java.browse/browse-url "https://soundcloud.com/meta-ex/machine-run")
(clojure.java.browse/browse-url "https://soundcloud.com/repl-electric"")
