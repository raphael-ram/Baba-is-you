# Baba Is You (Java Clone)

Un projet de réimplémentation du célèbre jeu de puzzle **Baba Is You** en Java, utilisant la bibliothèque graphique Zen 6.

## 🎮 Comment Jouer

Le but du jeu est simple : **changer les règles pour gagner**.

Vous contrôlez un personnage (généralement **Baba**) et vous devez atteindre l'objet défini comme **WIN** (généralement le drapeau). Pour ce faire, vous pouvez pousser des blocs de texte pour former de nouvelles phrases et ainsi modifier les lois de la physique du niveau.

### Contrôles
- **Flèches directionnelles** : Se déplacer.
- **ESC** : Quitter le jeu.
- **R** : Recommencer le niveau (si implémenté) ou relancer le jeu.

## 📜 Règles et Mécaniques

Le jeu repose sur des blocs de mots (Sujet, Verbe, Complément) qui forment des règles lorsqu'ils sont alignés (horizontalement ou verticalement).

### Règles de Base
- **YOU** : L'objet que vous contrôlez (ex: `BABA IS YOU`).
- **WIN** : L'objectif à atteindre pour gagner le niveau (ex: `FLAG IS WIN`).
- **STOP** : Un obstacle infranchissable (ex: `WALL IS STOP`).
- **PUSH** : L'objet peut être poussé (ex: `ROCK IS PUSH`).

### Règles Avancées
- **MELT / HOT** : Un objet `HOT` détruit un objet `MELT` s'ils se touchent (ex: `LAVA IS HOT` et `WALL IS MELT`).
- **SINK** : Un objet `SINK` détruit tout objet qui le touche, et se détruit lui-même (ex: `WATER IS SINK`).
- **DEFEAT** : Un objet `DEFEAT` vous tue si vous le touchez (ex: `SKULL IS DEFEAT`).
- **PULL** : Si vous vous éloignez d'un objet `PULL`, il est tiré avec vous (ex: `BOX IS PULL`).

## 🚀 Installation et Lancement

### Prérequis
- Java JDK installé (version 17 ou supérieure recommandée).
- La bibliothèque `zen-6.0.jar` (incluse dans le dossier `lib`).

### Lancer le Jeu (Interface Graphique)

1. **Compiler** :
   ```bash
   javac -d bin -cp lib/zen-6.0.jar -sourcepath src src/projet/ControllerConsole/GameController.java
   ```

2. **Exécuter** :
   ```bash
   java -cp bin:lib/zen-6.0.jar projet.ControllerConsole.GameController
   ```

3. **Choisir un niveau** :
   Au lancement, un menu dans la console vous demandera de choisir un niveau (tapez le numéro et Entrée) :
   - **1-6** : Niveaux de difficulté croissante.
   - **7** : Niveau personnalisé complexe (Custom Level).

### Lancer en Mode Console (Texte)

Si vous ne pouvez pas afficher l'interface graphique :
```bash
javac -d bin -cp lib/zen-6.0.jar -sourcepath src src/Main.java
java -cp bin:lib/zen-6.0.jar Main
```

## 📂 Structure des Niveaux

Les niveaux sont stockés dans `src/external/`. Vous pouvez créer vos propres niveaux en modifiant `level_custom.txt` ou en créant de nouveaux fichiers `.txt`.

---
*Projet réalisé dans le cadre d'un exercice de programmation Java.*
