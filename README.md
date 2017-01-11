# rashomon

Rashomon helps you reconstruct a Clojure datastructure from a list of events.

## Inspiration

The term Rashomon effect is used to describe the situation when multiple observers have different, and often contraditory, interpretations of the same event. 

It is named for Kurosawa's classic 1950 film 'Rashomon', where four witnesses to a man's murder offer contraditory statements at the subsequent trial.

## Usage

```clojure
(require '[rashomon.core :as r])

(def ramifications
  {"NewTransaction"
   (fn [_ event]
     {:transaction/id (:transaction/id event)})
   "UpdateTransactionAmount"
   (fn [agg event]
     (assoc agg :transaction/amount
            (:transaction/amount event)))})

(r/rebuild ramifications
           [{:event/type "NewTransaction"
             :transaction/id 342}
            {:event/type "UpdateTransactionAmount"
             :transaction/amount 35}
            {:event/type "UpdateTransactionAmount"
             :transaction/amount 37}])
```

## Ideas for further development

## License

Copyright © 2017 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
