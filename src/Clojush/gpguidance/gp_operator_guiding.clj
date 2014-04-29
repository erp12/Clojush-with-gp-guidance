(ns clojush.gpguidance.gp-operator-guiding
  (:use [clojush.gpguidance.error-and-gp-operator-recording]))

(defn rand-vec-sum-one
  [size]
  (let [init-probs (vec (repeatedly size #(rand 1)))
        sum-init-probs (apply + init-probs)
        normalized-probs (map #(/ % sum-init-probs) init-probs)]
    (vec normalized-probs)))

(defn make-sum-one
  [argmap]
  (let [sum (+  (:reproduction-probability argmap) (:mutation-probability argmap)
                (:crossover-probability argmap) (:simplification-probability argmap)
                (:ultra-probability argmap) (:gaussian-mutation-probability argmap)
                (:deletion-mutation-probability argmap) (:boolean-gsxover-probability argmap)
                (:parentheses-addition-mutation-probability argmap))]
    (-> (assoc argmap :reproduction-probability (/ (:reproduction-probability argmap) sum))
      (assoc :mutation-probability (/ (:mutation-probability argmap) sum))
      (assoc :mutation-probability (/ (:mutation-probability argmap) sum))
      (assoc :reproduction-probability (/ (:reproduction-probability argmap) sum))
      (assoc :reproduction-probability (/ (:reproduction-probability argmap) sum))
      (assoc :reproduction-probability (/ (:reproduction-probability argmap) sum))
      (assoc :reproduction-probability (/ (:reproduction-probability argmap) sum))
      (assoc :reproduction-probability (/ (:reproduction-probability argmap) sum))
      (assoc :reproduction-probability (/ (:reproduction-probability argmap) sum)))))

(defn set-rand-operator-probs
  [argmap]
  (print "Setting rand operators")
  (let [rand-probs (rand-vec-sum-one 9)]
    (->
      (assoc argmap :reproduction-probability (first rand-probs))
      (assoc :mutation-probability (second rand-probs))
      (assoc :crossover-probability (nth rand-probs 2))
      (assoc :simplification-probability (nth rand-probs 3))
      (assoc :ultra-probability  (nth rand-probs 4))
      (assoc :gaussian-mutation-probability (nth rand-probs 5))
      (assoc :deletion-mutation-probability (nth rand-probs 6))
      (assoc :boolean-gsxover-probability (nth rand-probs 7))
      (assoc :parentheses-addition-mutation-probability (nth rand-probs 8)))))

(defn tweak-gp-operator
  [argmap gp-op direction]
  (assoc argmap gp-op (+ (get argmap gp-op) (* direction (:gp-guidance-learning-rate argmap)))))

(defn get-direction-to-tweak
  [last-better? gp-op]
  (if last-better?
    (let [better-probs (first (:past-genetic-operators-probs @gp-guidance-info))
          worse-probs (second (:past-genetic-operators-probs @gp-guidance-info))]
      (if (< (better-probs gp-op) (worse-probs gp-op)) 
        1 
        -1))
    (let [worse-probs (first (:past-genetic-operators-probs @gp-guidance-info))
          better-probs (second (:past-genetic-operators-probs @gp-guidance-info))]
      (if (< (better-probs gp-op) (worse-probs gp-op)) 
        1
        -1))))
  
  
(defn guide-gp-operators
  [argmap]
  (let [last-delta-average (:last-delta-average-error @gp-guidance-info)
        this-delta-average (:this-delta-average-error @gp-guidance-info)]
    (cond
      (< last-delta-average this-delta-average) (-> (tweak-gp-operator argmap :reproduction-probability 
                                                                       (get-direction-to-tweak true :reproduction-probability))
                                                  (tweak-gp-operator :mutation-probability 
                                                                     (get-direction-to-tweak true :mutation-probability))
                                                  (tweak-gp-operator :crossover-probability
                                                                     (get-direction-to-tweak true :crossover-probability))
                                                  (tweak-gp-operator :simplification-probability 
                                                                     (get-direction-to-tweak true :simplification-probability))
                                                  (tweak-gp-operator :ultra-probability 
                                                                     (get-direction-to-tweak true :ultra-probability))
                                                  (tweak-gp-operator :gaussian-mutation-probability 
                                                                     (get-direction-to-tweak true :gaussian-mutation-probability))
                                                  (tweak-gp-operator :boolean-gsxover-probability 
                                                                     (get-direction-to-tweak true :boolean-gsxover-probability))
                                                  (tweak-gp-operator :deletion-mutation-probability 
                                                                     (get-direction-to-tweak true :deletion-mutation-probability))
                                                  (tweak-gp-operator :parentheses-addition-mutation-probability 
                                                                     (get-direction-to-tweak true :parentheses-addition-mutation-probability)))
      (> last-delta-average this-delta-average) (-> (tweak-gp-operator argmap :reproduction-probability 
                                                                       (get-direction-to-tweak false :reproduction-probability))
                                                  (tweak-gp-operator :mutation-probability 
                                                                     (get-direction-to-tweak false :mutation-probability))
                                                  (tweak-gp-operator :crossover-probability
                                                                     (get-direction-to-tweak false :crossover-probability))
                                                  (tweak-gp-operator :simplification-probability 
                                                                     (get-direction-to-tweak false :simplification-probability))
                                                  (tweak-gp-operator :ultra-probability 
                                                                     (get-direction-to-tweak false :ultra-probability))
                                                  (tweak-gp-operator :gaussian-mutation-probability 
                                                                     (get-direction-to-tweak false :gaussian-mutation-probability))
                                                  (tweak-gp-operator :boolean-gsxover-probability 
                                                                     (get-direction-to-tweak false :boolean-gsxover-probability))
                                                  (tweak-gp-operator :deletion-mutation-probability 
                                                                     (get-direction-to-tweak false :deletion-mutation-probability))
                                                  (tweak-gp-operator :parentheses-addition-mutation-probability 
                                                                     (get-direction-to-tweak false :parentheses-addition-mutation-probability)))
      :else argmap)))



