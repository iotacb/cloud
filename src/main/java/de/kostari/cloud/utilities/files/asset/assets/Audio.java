package de.kostari.cloud.utilities.files.asset.assets;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.openal.AL10;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.libc.LibCStdlib;

public class Audio extends Asset {

    public Audio(String path, String name, boolean loop) {
        super(path, name);
        MemoryStack.stackPush();
        IntBuffer channelsBuffer = MemoryStack.stackMallocInt(1);
        MemoryStack.stackPush();
        IntBuffer sampleRateBuffer = MemoryStack.stackMallocInt(1);

        ShortBuffer rawAudioBuffer = STBVorbis.stb_vorbis_decode_filename(path,
                channelsBuffer, sampleRateBuffer);

        if (rawAudioBuffer == null) {
            System.out.println("Could not load sound '" + path + "'");
            MemoryStack.stackPop();
            MemoryStack.stackPop();
            return;
        }

        int channels = channelsBuffer.get();
        int sampleRate = sampleRateBuffer.get();

        MemoryStack.stackPop();
        MemoryStack.stackPop();

        int openALFormat = -1;
        if (channels == 1) {
            openALFormat = AL10.AL_FORMAT_MONO16;
        } else if (channels == 2) {
            openALFormat = AL10.AL_FORMAT_STEREO16;
        }

        bufferId = AL10.alGenBuffers();

        AL10.alBufferData(bufferId, openALFormat, rawAudioBuffer, sampleRate);

        sourceId = AL10.alGenSources();

        AL10.alSourcei(sourceId, AL10.AL_BUFFER, bufferId);
        AL10.alSourcei(sourceId, AL10.AL_LOOPING, loop ? 1 : 0);
        AL10.alSourcei(sourceId, AL10.AL_POSITION, 0);
        AL10.alSourcef(sourceId, AL10.AL_GAIN, 0.3f);

        LibCStdlib.free(rawAudioBuffer);
    }

    private int bufferId;
    private int sourceId;

    private boolean playing;

    public void delete() {
        AL10.alDeleteSources(sourceId);
        AL10.alDeleteBuffers(bufferId);
    }

    public void play() {
        int state = AL10.alGetSourcei(sourceId, AL10.AL_SOURCE_STATE);
        if (state == AL10.AL_STOPPED) {
            playing = false;
            AL10.alSourcei(sourceId, AL10.AL_POSITION, 0);
        }

        if (!playing) {
            AL10.alSourcePlay(sourceId);
            playing = true;
        } else {
            stop();
            AL10.alSourcePlay(sourceId);
            playing = true;
        }
    }

    public void stop() {
        if (playing) {
            AL10.alSourceStop(sourceId);
            playing = false;
        }
    }

    /**
     * 
     * @return true if the audio is playing
     */
    public boolean isPlaying() {
        int state = AL10.alGetSourcei(sourceId, AL10.AL_SOURCE_STATE);
        if (state == AL10.AL_STOPPED) {
            playing = false;
        }
        return playing;
    }

}
