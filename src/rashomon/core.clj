(ns rashomon.core
  (:require [clojure.spec :as s]
            [clojure.spec.test :as t]
            [clojure.spec.gen :as g]))

(defn find-ram
  [rams event-type]
  (let [ram (get rams event-type)]
    (if ram
      ram
      (throw (Exception.
              (str "Ramification not found. "
                   ":event/type " event-type))))))

(defn rams->fn
  [rams]
  (fn [agg event]
    (let [event-type (:event/type event)]
      (if-not event-type
        (throw (Exception. ":event/type is nil or false."))
        (let [apply-event (find-ram rams event-type)]
          (apply-event agg event))))))

(defn rebuild
  "Rebuilds an aggregate from the events provided. Events must have an :event/type that is a key in the ramifications provided."
  [rams events]
  (let [apply-events (rams->fn rams)]
    (reduce apply-events nil events)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;      SPEC      ;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(s/def :event/type string?)
(s/def ::event (s/keys :req [:event/type]))
(s/def ::events (s/coll-of ::event))
(s/def ::rams (s/keys))
(s/def ::aggregate (s/keys :req [:aggregate/id]))

(defn event-types-match-keys?
  [rams events]
  (every? #(contains? rams (:event/type %))
          events))

(s/fdef rebuild
        :args (s/and (s/cat :rams ::rams :events ::events)
                     #(event-types-match-keys?
                             (:rams %) (:events %)))
        :ret (s/nilable ::aggregate)
        :fn #(= (:aggregate/id (:ret %))
                (:aggregate/id (first (:args %)))))

;; (t/check `rebuild)

