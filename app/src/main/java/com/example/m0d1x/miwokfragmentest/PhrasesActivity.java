package com.example.m0d1x.miwokfragmentest;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class PhrasesActivity extends AppCompatActivity {
    ListView listview;
    AudioManager mAudioManager;
    ArrayList<dictionary> Phrases = new ArrayList<>();


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
    //Handles playback of all the sound files
    private MediaPlayer mMediaPlayer;
    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            mMediaPlayer.release();
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
        setContentView(R.layout.activity_phrases);
        //Adding Back Button to our bar

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Adding Audio manager

        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        listview = (ListView) findViewById(R.id.ListView_phrases);

        // Create a list of words
        Phrases.add(new dictionary(getString(R.string.phrase_where_are_you_going), "minto wuksus", R.drawable.question, R.raw.phrase_where_are_you_going));
        Phrases.add(new dictionary(getString(R.string.phrase_whats_your_name), "tinnә oyaase'nә", R.drawable.question, R.raw.phrase_what_is_your_name));
        Phrases.add(new dictionary(getString(R.string.phrase_my_name_is), "oyaaset", R.drawable.question, R.raw.phrase_my_name_is));
        Phrases.add(new dictionary(getString(R.string.phrase_how_are_you_feeling), "michәksәs", R.drawable.question, R.raw.phrase_how_are_you_feeling));
        Phrases.add(new dictionary(getString(R.string.phrase_i_am_feeling_good), "kuchi achit", R.drawable.question, R.raw.phrase_im_feeling_good));
        Phrases.add(new dictionary(getString(R.string.phrase_are_you_coming), "әәnәs'aa?", R.drawable.question, R.raw.phrase_are_you_coming));
        Phrases.add(new dictionary(getString(R.string.phrase_yes_i_am_coming), "hәә’ әәnәm", R.drawable.question, R.raw.phrase_yes_im_coming));
        Phrases.add(new dictionary(getString(R.string.phrases_I_m_coming), "әәnәm", R.drawable.question, R.raw.phrase_im_coming));
        Phrases.add(new dictionary(getString(R.string.phrases_Lets_go), "yoowutis", R.drawable.question, R.raw.phrase_lets_go));
        Phrases.add(new dictionary(getString(R.string.phrases_come_here), "әnni'nem", R.drawable.question, R.raw.phrase_come_here));


        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // costum_layout.xml layout file.
        DictionaryAdapter dictionary = new DictionaryAdapter(this, R.layout.costom_layout, Phrases, R.color.category_phrases);

        // Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Word} in the list.
        listview.setAdapter(dictionary);

        // Set a click listener to play the audio when the list item is clicked on
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
                    mMediaPlayer = MediaPlayer.create(PhrasesActivity.this, Phrases.get(position).getSoundID());

                    // Start the audio file
                    mMediaPlayer.start();

                    // Setup a listener on the media player, so that we can stop and release the
                    // media player once the sound has finished playing.
                    mMediaPlayer.setOnCompletionListener(onCompletionListener);
                }
            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();
        // When the activity is stopped, release the media player resources because we won't
        // be playing any more sounds.
        releaseMediaPlayer();
    }


}
