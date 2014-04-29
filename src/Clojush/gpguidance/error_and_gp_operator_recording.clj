(ns clojush.gpguidance.error-and-gp-operator-recording)

(def gp-guidance-info (atom {:past-average-errors [:need-more-gens :need-more-gens :need-more-gens]
                             :past-genetic-operators-probs [{}
                                                            {}
                                                            {}]
                             :last-delta-average-error :not-set-yet
                             :this-delta-average-error :not-set-yet}))

(defn update-past-average-errors
  [new-average-error]
  (reset! gp-guidance-info (assoc @gp-guidance-info
                                  :past-average-errors
                                  (vec (rest (conj (:past-average-errors @gp-guidance-info) new-average-error))))))

(defn update-past-genetic-operator-probs
  [new-genetic-operator-probs]
  (reset! gp-guidance-info (assoc @gp-guidance-info
                                  :past-genetic-operators-probs
                                  (vec (rest (conj (:past-genetic-operators-probs @gp-guidance-info) new-genetic-operator-probs))))))

(defn set-delta-average-errors
  []
  (reset! gp-guidance-info (assoc @gp-guidance-info
                                  :last-delta-average-error
                                  (- (second (:past-average-errors @gp-guidance-info))
                                     (first (:past-average-errors @gp-guidance-info)))))
  (reset! gp-guidance-info (assoc @gp-guidance-info
                                  :this-delta-average-error
                                  (- (nth (:past-average-errors @gp-guidance-info) 2)
                                     (second (:past-average-errors @gp-guidance-info))))))

(defn write-gp-guidance-info
  [gen-number]
  (doseq
    [s [(clojure.string/join ["Generation: " (str gen-number) "\n"])
        (clojure.string/join ["Past Average Errors: " (:past-average-errors @gp-guidance-info) "\n"])
        (clojure.string/join ["Past Genetic Operator Probs: " (:past-genetic-operators-probs @gp-guidance-info) "\n"])
        (clojure.string/join ["Last Delta Average Error: " (:last-delta-average-error @gp-guidance-info) "\n"])
        (clojure.string/join ["This Delta Average Error: " (:this-delta-average-error @gp-guidance-info) "\n"])]]
  (spit (clojure.string/join ["GPguidanceReports/" (str gen-number) ".txt"]) s :append true)))