(ns rashomon.core-test
  (:require [clojure.test :refer :all]
            [rashomon.core :refer :all]))

(def rams {"AggregateCreated"
          (fn [agg event]
            (assoc agg
                   :aggregate/id
                   (:aggregate/id event)))
          "AmountChanged"
          (fn [agg event]
            (assoc agg
                   :aggregate/amount
                   (:aggregate/amount event)))
          "AmountDecreased"
          (fn [agg event]
            (update agg :aggregate/amount
                    #(- (or % 0) %2)
                    (:aggregate/decrease event)))})

(deftest rebuild-test
  (testing "Can create an aggregate."
    (is (= {:aggregate/id "32"}
           (rebuild rams [{:event/type "AggregateCreated"
                          :aggregate/id "32"}]))))

  (testing "Can add an amount."
    (is (= {:aggregate/id "32" :aggregate/amount 54}
           (rebuild rams [{:event/type "AggregateCreated"
                          :aggregate/id "32"}
                         {:event/type "AmountChanged"
                          :aggregate/amount 54}]))))

  (testing "Decrease on unexisting amount."
    (is (= {:aggregate/id "32" :aggregate/amount -4}
           (rebuild rams [{:event/type "AggregateCreated"
                          :aggregate/id "32"}
                         {:event/type "AmountDecreased"
                          :aggregate/decrease 4}])))))
