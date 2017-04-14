# Rashomon

Rashomon helps you reconstruct a Clojure datastructure from a list of events.

## Inspiration

The term 'Rashomon effect' is used to describe the situation when multiple observers have different, and often contraditory, interpretations of the same event. 

It is named for Kurosawa's classic 1950 film 'Rashomon', where four witnesses to a man's murder offer contraditory statements at the subsequent trial.

### Glossary

Rashomon uses the word "perspective" for the function associated with an event, and the word "testimony" to describe the datastructure produced by rebuilding -- in homage to its eponym.

## Usage

```clojure
(require '[rashomon.core :as r])

(def perspectives
  {:rashomon.event.type/txn-created
   (fn [_ event]
     {:transaction/id (:transaction/id event)})
   :rashomon.event.type/txn-amount-updated
   (fn [txn event]
     (assoc agg :transaction/amount
            (:transaction/amount event)))})

(r/rebuild perspectives
           [{:rashomon.event/type :rashomon.event.type/txn-created
             :transaction/id 342}
            {:rashomon.event/type :rashomon.event.type/txn-amount-updated
             :transaction/amount 32}
            {:rashomon.event/type :rashomon.event.type/txn-amount-updated
             :transaction/amount 36}])
```

### Applying events

We can also apply events to existing testimonies.

```clojure
(r/apply-event {:transaction/id 76
                :transaction/amount 5}
               perspectives
               [{:rashomon.event/type :rashomon.event.type/txn-amount-updated
                 :transaction/amount 10}])
```

### NB

- the perspectives must be a function accepting two arguments, the first being the testimony and the second being the event.
- the events must be labelled with the key `:rashomon.event/type`
- when rebuilding, the initial event must be able to be applied to a `nil` testimony.
