# 🎵 Music Setup Guide for Kids Learning App

This guide explains how to add background music files to the Kids Learning app so children can enjoy
exciting learning experiences with music!

## Overview

The app now includes a **BackgroundMusicPlayer** system that plays relaxing and energizing music
while kids are tracing letters and learning. Children can toggle the music on/off with the music
button in the drawing screen.

## Music Files Location

Place your music files in the following directory:

```
app/src/main/res/raw/
```

## Supported Music Formats

- **MP3** (recommended - best compatibility)
- **WAV**
- **OGG**

## Available Music Tracks

The system supports 5 different music tracks:

1. **happy_learning_music** (currently used by default)
    - Happy, uplifting melody perfect for motivation

2. **gentle_piano_music**
    - Calm, soothing piano for focused learning

3. **uplifting_melody**
    - Energetic instrumental for excitement

4. **cheerful_bells**
    - Light, playful bell sounds

5. **rainbow_notes**
    - Colorful, imaginative music

## How to Add Music Files

### Step 1: Find or Create Music Files

You can:

- Use royalty-free music from sites like:
    - [FreeMusic Archive](https://freemusicarchive.org/)
    - [Incompetech](https://incompetech.com/)
    - [YouTube Audio Library](https://www.youtube.com/audiolibrary)
- Use child-friendly music collections
- Create your own custom melodies

### Step 2: Optimize the Audio Files

For mobile apps, optimize your music files:

- **Bitrate**: 128 kbps (good balance of quality and size)
- **Sample rate**: 44.1 kHz
- **Duration**: 30-120 seconds recommended (will loop)

Use tools like:

- FFmpeg
- Audacity (free)
- MP3 converters

**Example using FFmpeg:**

```bash
ffmpeg -i input_music.mp3 -b:a 128k -ar 44100 happy_learning_music.mp3
```

### Step 3: Place Files in Project

1. In Android Studio, go to: `app/src/main/res/raw/`
2. Right-click → New → File
3. Name it exactly: `happy_learning_music.mp3` (or other track name)
4. Copy/paste your optimized audio file

Or manually copy files to the folder via file explorer.

### Step 4: Verify Resource Names

The resource names in code MUST match the file names exactly:

```kotlin
enum class MusicTrack(val resourceName: String, val displayName: String) {
    HAPPY_LEARNING("happy_learning_music", "Happy Learning"),
    GENTLE_PIANO("gentle_piano_music", "Gentle Piano"),
    UPLIFTING_MELODY("uplifting_melody", "Uplifting Melody"),
    CHEERFUL_BELLS("cheerful_bells", "Cheerful Bells"),
    RAINBOW_NOTES("rainbow_notes", "Rainbow Notes");
}
```

**File naming rule**: Remove `.mp3` extension from the enum value.

## How the System Works

### Music Playback

- Music starts automatically when the child enters the drawing screen
- Default volume: **50%** (doesn't drown out letter pronunciation)
- Music **loops continuously** while drawing
- Perfect for keeping children engaged and excited to learn!

### User Controls

- **Music Button** (cyan colored) in the toolbar toggles music on/off
- Shows toast notifications: "Musique en cours..." / "Musique en pause"
- Music pauses when activity is paused (phone locked, switched apps)
- Music resumes when returning to the activity

## Code Implementation Details

### BackgroundMusicPlayer Class

Located in: `app/src/main/java/com/kidslearning/app/utils/BackgroundMusicPlayer.kt`

**Key Methods:**

```kotlin
// Start playing with specific track and volume (0-1)
startMusic(track: MusicTrack, volume: Float)

// Pause/Resume
pauseMusic()
resumeMusic()

// Stop and release
stopMusic()
release()

// Control
setVolume(volume: Float)
setLooping(loop: Boolean)
isPlaying(): Boolean
```

### DrawingActivity Integration

The music player is integrated into `DrawingActivity`:

- Initialized in `onCreate()`
- Started automatically with `startBackgroundMusic()`
- Toggled via `toggleBackgroundMusic()` when user clicks music button
- Paused in `onPause()`
- Released in `onDestroy()`

## Customizing Music

### Change Default Track

Edit `DrawingActivity.kt`:

```kotlin
private fun startBackgroundMusic() {
    backgroundMusicPlayer.startMusic(
        track = BackgroundMusicPlayer.MusicTrack.GENTLE_PIANO,  // Change this
        volume = 0.5f
    )
}
```

### Change Default Volume

```kotlin
backgroundMusicPlayer.startMusic(
    track = BackgroundMusicPlayer.MusicTrack.HAPPY_LEARNING,
    volume = 0.7f  // 70% volume
)
```

### Change Volume Dynamically

```kotlin
backgroundMusicPlayer.setVolume(0.3f)  // 30% volume
```

## Troubleshooting

### Music doesn't play

1. ✅ Check file exists in `app/src/main/res/raw/`
2. ✅ File name matches exactly in `MusicTrack` enum
3. ✅ File is in supported format (MP3, WAV, OGG)
4. ✅ Device has volume enabled (not muted)
5. ✅ Try rebuilding the project

### Music is too loud/quiet

- Adjust volume in `startMusic()` call (0f = silent, 1f = max)
- Current default is 0.5f = 50%

### Music cuts off when app switches

- This is expected behavior - music pauses in `onPause()`
- Resumes when returning to app in `onResume()`

### File size too large

- Compress using FFmpeg (see Step 2)
- Target: 200-500 KB per file
- Optimize bitrate to 128 kbps

## Best Practices for Kids Learning Music

✅ **Good Music Characteristics:**

- Uplifting and positive tone
- No vocals (or soft, gentle vocals only)
- 60-100 BPM tempo (not too fast, not too slow)
- No sudden loud noises
- Repeatable/loopable structure

✅ **Good Genres:**

- Instrumental pop
- Classical/Piano
- Lo-fi hip hop
- Ambient/Chill
- World music
- Children's music collections

❌ **Avoid:**

- Heavy metal or aggressive music
- Music with lyrics that distract from learning
- Extremely long tracks (memory intensive)
- Poor audio quality

## Adding Music to Other Activities

To add background music to other learning activities:

```kotlin
private lateinit var backgroundMusicPlayer: BackgroundMusicPlayer

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // ... other code ...
    
    backgroundMusicPlayer = BackgroundMusicPlayer(this)
    backgroundMusicPlayer.startMusic(BackgroundMusicPlayer.MusicTrack.HAPPY_LEARNING, 0.5f)
}

override fun onDestroy() {
    super.onDestroy()
    backgroundMusicPlayer.release()
}
```

## Resources

### Free Music Sources

- [FreeMusic Archive](https://freemusicarchive.org/) - Creative Commons music
- [Incompetech](https://incompetech.com/) - Royalty-free instrumental
- [YouTube Audio Library](https://www.youtube.com/audiolibrary) - Free for YouTube creators
- [BenSound](https://www.bensound.com/) - Uplifting background music

### Audio Editing Tools

- [Audacity](https://www.audacityteam.org/) - Free audio editor
- [FFmpeg](https://ffmpeg.org/) - Command-line audio tool
- [Online-Convert](https://online-convert.com/) - Quick conversions

## Questions?

For more details on the BackgroundMusicPlayer implementation, see the inline documentation in:
`app/src/main/java/com/kidslearning/app/utils/BackgroundMusicPlayer.kt`
