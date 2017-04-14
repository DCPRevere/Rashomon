(ns rashomon.core
  (:require [clojure.spec :as s]
            [clojure.spec.gen :as g]))

(defn persp->fn
  "Converts the perspectives map into a reducing function."
  [perspectives]
  (fn [testimony event]
    (let [event-type (:rashomon.event/type event)
          perspective (get perspectives event-type)]
      (if (nil? perspective)
        testimony
        (perspective testimony event)))))

(defn apply-events
  [testimony perspectives events]
  (let [fun (persp->fn perspectives)]
    (reduce fun testimony events)))

(defn rebuild
  [perspectives events]
  (apply-events nil perspectives events))
