# 🚀 Guide d'Installation - Kids Learning

## 📋 Prérequis Système

### Logiciels Requis
1. **Android Studio** (Hedgehog 2023.1.1 ou supérieur)
   - Télécharger: https://developer.android.com/studio
   
2. **Java Development Kit (JDK) 17**
   - Inclus avec Android Studio
   - Vérifier: `java -version`

3. **Android SDK**
   - SDK Platform 34 (Android 14)
   - SDK Build-Tools 34.0.0
   - Android SDK Command-line Tools

### Configuration Minimale
- **Système**: Windows 10/11, macOS 10.14+, ou Linux
- **RAM**: 8 GB minimum (16 GB recommandé)
- **Espace disque**: 10 GB libres
- **Résolution**: 1280 x 800 minimum

## 📦 Installation Étape par Étape

### Étape 1: Extraire le Projet

```bash
# Décompresser le fichier ZIP
unzip KidsLearning.zip

# Accéder au répertoire
cd KidsLearning
```

### Étape 2: Ouvrir dans Android Studio

1. Lancer **Android Studio**
2. Cliquer sur **File** > **Open**
3. Sélectionner le dossier `KidsLearning`
4. Cliquer sur **OK**

### Étape 3: Synchronisation Gradle

Android Studio va automatiquement:
- Télécharger les dépendances
- Configurer le projet
- Indexer les fichiers

**⏱️ Temps estimé**: 2-5 minutes (selon la connexion internet)

Si la synchronisation ne démarre pas automatiquement:
- Cliquer sur **File** > **Sync Project with Gradle Files**

### Étape 4: Configuration du SDK

Si Android Studio demande de configurer le SDK:

1. Aller dans **File** > **Project Structure** > **SDK Location**
2. Vérifier que Android SDK est installé
3. Dans **SDK Manager** (icône 📦):
   - Onglet **SDK Platforms**: Cocher Android 14.0 (API 34)
   - Onglet **SDK Tools**: Vérifier que Build-Tools 34.0.0 est installé

### Étape 5: Ajouter les Fichiers Audio (Optionnel)

⚠️ **Important**: L'application fonctionne sans fichiers audio mais sans sons.

#### Structure requise:
```
app/src/main/assets/sounds/
├── alif.mp3
├── ba.mp3
├── ta.mp3
├── ... (autres lettres arabes)
├── a.mp3
├── b.mp3
├── c.mp3
└── ... (autres lettres françaises)
```

#### Comment ajouter:

**Méthode 1: Via Android Studio**
1. Clic droit sur `app/src/main/assets`
2. New > Directory > Nommer "sounds"
3. Glisser-déposer vos fichiers .mp3 dans ce dossier

**Méthode 2: Manuellement**
1. Créer le dossier: `app/src/main/assets/sounds/`
2. Copier tous les fichiers .mp3 dans ce dossier

#### Où trouver des sons gratuits:
- **Freesound.org**: https://freesound.org/
- **Text-to-Speech en ligne**: https://ttsmp3.com/
- **Enregistrement personnel**: Utiliser un smartphone

### Étape 6: Préparer un Appareil

#### Option A: Émulateur Android (Recommandé pour tester)

1. Cliquer sur **Device Manager** (icône 📱)
2. Cliquer sur **Create Device**
3. Sélectionner un appareil:
   - **Téléphone**: Pixel 5 ou Pixel 7
   - **Tablette**: Pixel Tablet
4. Choisir une image système:
   - **API Level 34** (Android 14.0)
   - Télécharger si nécessaire
5. Nommer l'émulateur et cliquer sur **Finish**

**⏱️ Premier téléchargement**: 5-10 minutes

#### Option B: Appareil Physique

1. **Activer les options développeur**:
   - Aller dans Paramètres > À propos du téléphone
   - Taper 7 fois sur "Numéro de build"

2. **Activer le débogage USB**:
   - Paramètres > Options développeur
   - Activer "Débogage USB"

3. **Connecter via USB**:
   - Brancher le téléphone
   - Autoriser le débogage sur l'appareil
   - Vérifier que l'appareil apparaît dans Android Studio

### Étape 7: Lancer l'Application

1. Sélectionner l'appareil (émulateur ou physique) dans la barre d'outils
2. Cliquer sur le bouton **Run** ▶️ (ou Shift+F10)
3. Attendre la compilation et le déploiement

**⏱️ Premier lancement**: 1-3 minutes

## ✅ Vérification de l'Installation

### L'application devrait:
- ✅ S'ouvrir sur l'écran d'accueil avec 2 cartes (Arabe et Français)
- ✅ Afficher les lettres en grille quand on clique sur une carte
- ✅ Ouvrir l'écran de dessin quand on clique sur une lettre
- ✅ Permettre de dessiner avec le doigt
- ✅ Jouer un son (si fichiers audio ajoutés)

## 🐛 Résolution des Problèmes

### Problème: Erreur de synchronisation Gradle

**Solution 1**: Nettoyer le projet
```bash
# Dans Android Studio
Build > Clean Project
Build > Rebuild Project
```

**Solution 2**: Invalider les caches
```bash
File > Invalidate Caches > Invalidate and Restart
```

**Solution 3**: Vérifier gradle.properties
- Ouvrir `gradle.properties`
- Vérifier que les paramètres sont corrects

### Problème: SDK non trouvé

**Solution**:
```bash
File > Project Structure > SDK Location
Vérifier que le chemin SDK est correct
```

### Problème: Erreur de compilation

**Erreur courante**: `kotlinOptions` not found
- Vérifier que vous utilisez bien les fichiers `.kts` (Kotlin DSL)

**Solution**:
- File > Sync Project with Gradle Files

### Problème: L'émulateur ne démarre pas

**Solution**:
1. Aller dans **AVD Manager**
2. Supprimer l'émulateur
3. Recréer avec les paramètres:
   - RAM: 2048 MB minimum
   - VM Heap: 512 MB
   - Activer "Hardware - GLES 2.0"

### Problème: Application crash au démarrage

**Causes possibles**:
1. Base de données corrompue
   - Désinstaller et réinstaller l'app
   
2. Fichier JSON manquant
   - Vérifier que `alphabet_data.json` existe dans `assets/`

3. Erreur de permission
   - Vérifier AndroidManifest.xml

## 📱 Tester sur Différents Appareils

### Téléphones Recommandés:
- Pixel 5 (API 34) - 1080 x 2340
- Pixel 7 (API 34) - 1080 x 2400

### Tablettes Recommandées:
- Pixel Tablet (API 34) - 2560 x 1600

### Tester le Responsive:
1. Créer plusieurs émulateurs avec différentes tailles
2. Vérifier que l'interface s'adapte correctement
3. Tester en mode portrait et paysage

## 🔧 Configuration Avancée

### Changer la Version de Kotlin

Dans `build.gradle.kts` (root):
```kotlin
plugins {
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
}
```

### Augmenter la Mémoire de Build

Dans `gradle.properties`:
```properties
org.gradle.jvmargs=-Xmx4096m -Dfile.encoding=UTF-8
```

### Accélérer les Builds

Dans `gradle.properties`, ajouter:
```properties
org.gradle.parallel=true
org.gradle.caching=true
kotlin.incremental=true
```

## 📊 Structure Finale du Projet

```
KidsLearning/
├── app/
│   ├── build.gradle.kts          ✅ Kotlin DSL
│   ├── src/
│   │   └── main/
│   │       ├── AndroidManifest.xml
│   │       ├── java/com/kidslearning/app/
│   │       ├── res/
│   │       └── assets/
│   │           ├── alphabet_data.json    ✅ Requis
│   │           └── sounds/              ⚠️ Optionnel
│   │               ├── alif.mp3
│   │               ├── a.mp3
│   │               └── ...
├── build.gradle.kts              ✅ Kotlin DSL
├── settings.gradle.kts           ✅ Kotlin DSL
├── gradle.properties
└── README.md
```

## 🎉 Installation Réussie!

Si tout est vert ✅, vous pouvez maintenant:
1. Explorer le code source
2. Personnaliser les couleurs et styles
3. Ajouter de nouvelles fonctionnalités
4. Tester sur différents appareils

## 📞 Besoin d'Aide?

### Ressources Utiles:
- **Documentation Android**: https://developer.android.com/docs
- **Kotlin DSL**: https://docs.gradle.org/current/userguide/kotlin_dsl.html
- **Room Database**: https://developer.android.com/training/data-storage/room

### Erreurs Fréquentes:
- Vérifier que toutes les dépendances sont téléchargées
- S'assurer d'avoir une connexion internet pour la première build
- Utiliser Android Studio à jour
- Vérifier que le JDK 17 est bien configuré

---

**Bon développement! 🚀**
