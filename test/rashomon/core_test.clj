(ns rashomon.core-test
  (:require [clojure.test :as t]
            [rashomon.core :as sut]))

(def arithmetic-persps
  {:rashomon.event.type/add
   (fn [testimony event]
     (let [n (:num event)]
       (if (nil? testimony)
         n
         (+ testimony n))))

   :rashomon.event.type/minus
   (fn [testimony event]
     (let [n (:num event)]
       (if (nil? testimony)
         (- n)
         (- testimony n))))

   :rashomon.event.type/abs
   (fn [testimony event]
     (if (nil? testimony)
       testimony
       (Math/abs testimony)))})

(t/deftest apply-events

  (t/testing "We can apply an event to an exisiting testimony."
    (t/is (= 5
             (sut/apply-events
              3
              arithmetic-persps
              [{:rashomon.event/type :rashomon.event.type/add :num 2}]))))

  (t/testing "We can apply multiple events to an existing testimony."
    (t/is (= 5
             (sut/apply-events
              -3
              arithmetic-persps
              [{:rashomon.event/type :rashomon.event.type/abs}
               {:rashomon.event/type :rashomon.event.type/add :num 2}])))))

(t/deftest rebuild

  (t/testing "We can rebuild a testimony from a single event."
    (t/is (= 5
             (sut/rebuild
              arithmetic-persps
              [{:rashomon.event/type :rashomon.event.type/add :num 5}]))))

  (t/testing "We can rebuild a testimony from multiple events."
    (t/is (= 8
             (sut/rebuild
              arithmetic-persps
              [{:rashomon.event/type :rashomon.event.type/add :num 10}
               {:rashomon.event/type :rashomon.event.type/minus :num 2}]))))

  (t/testing "Providing no events will return nil"
    (t/is (= nil
             (sut/rebuild arithmetic-persps []))))

  (t/testing "Events that are not in the perspectives will be applied"
    (t/is (= 5
             (sut/rebuild
              arithmetic-persps
              [{:rashomon.event/type :rashomon.event.type/add :num 5}
               {:rashomon.event/type :rashomon.event.type/fake :bar 25}])))
    (t/is (= -4
             (sut/rebuild
              arithmetic-persps
              [{:rashomon.event/type :rashomon.event.type/fake :bar 25}
               {:rashomon.event/type :rashomon.event.type/minus :num 4}]))))

  (t/testing "Events are applied in the correct order."
    (t/is (= 5
             (sut/rebuild
              arithmetic-persps
              [{:rashomon.event/type :rashomon.event.type/minus :num 5}
               {:rashomon.event/type :rashomon.event.type/abs}])))))
