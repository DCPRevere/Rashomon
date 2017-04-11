(ns rashomon.core-test
  (:require [clojure.test :as t]
            [rashomon.core :as sut]))

(t/deftest persp->fn
  (t/testing ""
    (t/is (= nil
             nil))))

(t/deftest rebuild
  (t/testing "We can rebuild a testimony from a list of events and perspectives."
    (t/is (= 5
             (sut/rebuild [{:rashomon.event/type :rashomon.event.type/add :num 5}]
                          {:rashomon.event.type/add
                           (fn [testimony event]
                             (let [n (:num event)]
                               (if (nil? testimony)
                                 n
                                 (+ n testimony))))})))))
