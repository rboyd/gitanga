# gitanga

> Not all bits have equal value.

> -- *Carl Sagan*

Low-level git interface for Clojure.

## Usage

```clojure
(require 'gitanga.util)
(require 'gloss.io)

(def obj "work/gitanga/.git/objects/c1/3c2e2914b5a427dc54e9761111c31243903c21")
(->> (gitanga.util/inflate obj)
     (gloss.io/decode gitanga.util/git-obj)
     pprint)
```

## Output

```
{:contents
 {:data "Parsing author/committer DateTime. Finishes #2\n",
  :committer
  {:string "Robert Boyd <rboyd@telematter.com> 1366081575 -0500",
   :date-time #<DateTime 2013-04-16T03:06:15.000-05:00>},
  :author
  {:string "Robert Boyd <rboyd@telematter.com> 1366081575 -0500",
   :date-time #<DateTime 2013-04-16T03:06:15.000-05:00>},
  :parents ["55beefa63c31ac5d8e59bf067d7e6fba7dc50ea8"],
  :tree-obj-id "2f51c03f5529fb1307c1a14f68cf7955f3a4909c",
  :type :commit-contents},
 :length 263,
 :type :commit}
```

## License

Copyright © 2013 Robert Boyd

Distributed under the Eclipse Public License, the same as Clojure.
