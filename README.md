# trickortreat_PuF
trick or treat spiel for Patterns and Frameworks


Da wir mit git arbeien empfiehlt es sich mit mit "git flow" zu arbeiten. Das ist ein git workflow mit dem man Ärgernisse mit git umschiffen kann, wenn man in einem Team arbeitet.
Eine Einführung findet ihr hier:
https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow

Die Kurzfassung: 
1. Es gibt einen Master Branch auf dem NICHT gearbeitet wird. Von diesem wird released. 
2. Als nächstes hat man einen "develop" Branch in diesen werden die Feature Branches gemerged.
3. Wenn Ihr etwas Entwickelt dann mit dem git Befehl "git checkout develop" auf den develop-Branch wechseln. Dann mit "git checkout -b <Name_des_Featurebranch>" auf einen eigenen Branch wechseln. (Habt ihr git flow installiert geht es auch per "git flow feature start <Name_des_Featurebranch>")
4. Ist die Entwicklung fertig dann wieder auf den develop-Branch wechseln und einen merge durchführen (1."git checkout develop" 2. "git merge feature_branch").



