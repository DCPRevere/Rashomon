(ns rashomon.core
  (:require [clojure.spec :as s]
            [clojure.spec.gen :as g]))

(defn persp->fn
  [perspectives]
  (fn [testimony event]
    (let [event-type (:rashomon.event/type event)
          perspective (get perspectives event-type)]
      (if (nil?)
        testimony
        (perspective testimony event)))))

(defn rebuild
  [events perspectives]
  (let [fun (persp->fn perspectives)]
    (reduce fun nil events)))
