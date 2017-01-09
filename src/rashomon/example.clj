(ns rashomon.example
  (:require [rashomon.core :as rashomon]))

(def created
  {:event/type "AggregateCreated"
   :aggregate/id "32"})

(def amount-changed
  {:event/type "AmountChanged"
   :aggregate/amount 54})

(def amount-decreased
  {:event/type "AmountDecreased"
   :aggregate/decrease 30})

(def rams
  {"AggregateCreated"
   (fn [_ event]
     (assoc {}
            :aggregate/id
            (:aggregate/id event)))
   "AmountChanged"
   (fn [agg event]
     (assoc agg
            :aggregate/amount
            (:aggregate/amount event)))
   "AmountDecreased"
   (fn [agg event]
     (update-in agg [:aggregate/amount]
                - (:aggregate/decrease event)))})

(rashomon/rebuild rams [created
                        amount-changed
                        amount-decreased
                        amount-decreased])
