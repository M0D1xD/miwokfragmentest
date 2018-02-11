package com.example.m0d1x.miwokfragmentest;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class FamilyActivity extends AppCompatActivity {
    ListView listview;
    ArrayList<dictionary> family = new ArrayList<>();
    AudioManager mAudioManager;


    //Handles playback of all the sound files
    private MediaPlayer mMediaPlayer;
    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            mMediaPlayer.release();
        }

    };

    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                // The AUDIOFOCUS_LOSS_TRANSIENT case means that we've lost audio focus for a
                // short amount of time. The AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK case means that
                // our app is allowed to continue playing sound but at a lower volume. We'll treat
                // both cases the same way because our app is playing short sound files.

                // Pause playback and reset player to the start of the file. That way, we can
                // play the word from the beginning when we resume playback.
                mMediaPlayer.pause();
                mMediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                mMediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // The AUDIOFOCUS_LOSS case means we've lost audio focus and
                // Stop playback and clean up resources
                releaseMediaPlayer();
            }
        }
    };

    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family);
        listview = (ListView) findViewById(R.id.ListView_family);
        //Adding Back Button to our bar

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Adding Audio manager

        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        // Create a list of words
        family.add(new dictionary(getString(R.string.family_father), "әpә", R.drawable.family_father, R.raw.family_father));
        family.add(new dictionary(getString(R.string.family_mother), "әṭa", R.drawable.family_mother, R.raw.family_mother));
        family.add(new dictionary(getString(R.string.family_son), "angsi", R.drawable.family_son, R.raw.family_son));
        family.add(new dictionary(getString(R.string.family_daughter), "tune", R.drawable.family_daughter, R.raw.family_daughter));
        family.add(new dictionary(getString(R.string.family_oldbrother), "taachi", R.drawable.family_older_brother, R.raw.family_older_brother));
        family.add(new dictionary(getString(R.string.family_YoungBrother), "chalitti", R.drawable.family_younger_brother, R.raw.family_younger_brother));
        family.add(new dictionary(getString(R.string.family_oldersister), "teṭe", R.drawable.family_older_sister, R.raw.family_older_sister));
        family.add(new dictionary(getString(R.string.family_YoungSister), "kolliti", R.drawable.family_younger_sister, R.raw.family_younger_sister));
        family.add(new dictionary(getString(R.string.family_grandmother), "ama", R.drawable.family_grandmother, R.raw.family_grandmother));
        family.add(new dictionary(getString(R.string.family_grandfather), "paapa", R.drawable.family_grandfather, R.raw.family_father));


        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // costum_layout.xml layout file.
        DictionaryAdapter dictionary = new DictionaryAdapter(this, R.layout.costom_layout, family, R.color.category_family);

        // Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Word} in the list.
        listview.setAdapter(dictionary);


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Release the media player if it currently exists because we are about to
                // play a different sound file
                releaseMediaPlayer();


                // Request audio focus so in order to play the audio file. The app needs to play a
                // short audio file, so we will request audio focus with a short amount of time
                // with AUDIOFOCUS_GAIN_TRANSIENT.
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // We have audio focus now.

                    // Create and setup the {@link MediaPlayer} for the audio resource associated
                    // with the current word
                    mMediaPlayer = MediaPlayer.create(FamilyActivity.this, family.get(position).getSoundID());

                    // Start the audio file
                    mMediaPlayer.start();

                    // Setup a listener on the media player, so that we can stop and release the
                    // media player once the sound has finished playing.
                    mMediaPlayer.setOnCompletionListener(onCompletionListener);
                }
            }
        });
    }
}
