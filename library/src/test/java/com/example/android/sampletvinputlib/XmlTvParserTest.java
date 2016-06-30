/*
 * Copyright 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.sampletvinputlib;

import android.media.tv.TvContract;

import com.example.android.sampletvinput.utils.TvContractUtils;
import com.example.android.sampletvinput.xmltv.XmlTvParser;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class XmlTvParserTest extends TestCase {
    @Test
    public void testChannelParsing() throws IOException, XmlTvParser.XmlTvParseException {
        String testXmlFile = "xmltv.xml";
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(testXmlFile);
        XmlTvParser.TvListing listings = XmlTvParser.parse(inputStream);
        assertEquals(4, listings.channels.size());
        assertEquals("Creative Commons", listings.channels.get(1).getDisplayName());
        assertEquals("2-3", listings.channels.get(2).getDisplayNumber());
        assertEquals("App Link Text 1", listings.channels.get(2).getAppLinkText());
        assertTrue(listings.channels.get(3).getChannelLogo()
                .contains("storage.googleapis.com/android-tv/images/mpeg_dash.png"));
    }

    @Test
    public void testProgramParsing() throws XmlTvParser.XmlTvParseException {
        String testXmlFile = "xmltv.xml";
        String APRIL_FOOLS_SOURCE = "https://commondatastorage.googleapis.com/android-tv/Sample%2" +
                "0videos/April%20Fool's%202013/Introducing%20Google%20Fiber%20to%20the%20Pole.mp4";
        String ELEPHANTS_DREAM_POSTER_ART = "https://storage.googleapis.com/gtv-videos-bucket/sam" +
                "ple/images_480x270/ElephantsDream.jpg";
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(testXmlFile);
        XmlTvParser.TvListing listings = XmlTvParser.parse(inputStream);
        assertEquals(9, listings.programs.size());
        assertEquals("Introducing Gmail Blue", listings.programs.get(0).getTitle());
        assertEquals(TvContract.Programs.Genres.TECH_SCIENCE,
                listings.programs.get(1).getCanonicalGenres()[1]);
        assertEquals(listings.programs.get(2).getChannelId(),
                listings.channels.get(0).getId());
        assertEquals(TvContractUtils.convertVideoInfoToInternalProviderData(
                TvContractUtils.SOURCE_TYPE_HTTP_PROGRESSIVE, APRIL_FOOLS_SOURCE),
                listings.programs.get(3).getInternalProviderData());
        assertEquals("Introducing Google Nose", listings.programs.get(4).getDescription());
        assertEquals(ELEPHANTS_DREAM_POSTER_ART, listings.programs.get(5).getPosterArtUri());
    }

    @Test
    public void testValidXmlParsing()
            throws XmlTvParser.XmlTvParseException, FileNotFoundException {
        String testXmlFile = "xmltv.xml";
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(testXmlFile);
        XmlTvParser.TvListing listings = XmlTvParser.parse(inputStream);
        // The parsing did not encounter any errors
        assertNotNull(listings);
        assertEquals(4, listings.channels.size());
        assertEquals(9, listings.programs.size());
    }

    @Test
    public void testInvalidXmlParsing() throws FileNotFoundException {
        String testXmlFile = "invalid_xmltv.xml";
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(testXmlFile);
        try {
            XmlTvParser.TvListing listings = XmlTvParser.parse(inputStream);
            // The parsing succeeded though it was not supposed to
            fail();
        } catch (XmlTvParser.XmlTvParseException e) {
            // The parser encountered an error and exposed it to the developer as expected
        }
    }

    @Test
    public void testInvalidPath() throws XmlTvParser.XmlTvParseException {
        String testXmlFile = "invalid_file.xml";
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(testXmlFile);
        assert inputStream == null;
        try {
            XmlTvParser.TvListing listings = XmlTvParser.parse(inputStream);
            // The parsing succeeded though it was not supposed to
            fail();
        } catch (IllegalArgumentException e) {
            // The parser encountered an error and exposed it to the developer as expected
        }
    }
}
