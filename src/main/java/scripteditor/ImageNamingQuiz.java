/**
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package scripteditor;

import java.awt.Font;
import java.awt.Image;
import java.text.DecimalFormat;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Administrator
 */
public class ImageNamingQuiz {
 
    SpellingClass spelling;
    int mMode;
    int xCenter;
    int mNameLabelY;
    int mNameLabelHeight;
    JLabel mName;
    JLabel mNameLabel;
    boolean Running = true;
    boolean mPaused = false;
    boolean mScriptMode = false;
    SessionInfo mSessionInfo;
    boolean italic=false;
    public ImageNamingQuiz(SessionInfo session, int mode, boolean script_mode) {
        mSessionInfo = session;
        mMode = mode;
        mScriptMode = script_mode;
    }

    public void RunWithPrompt(){
        Main(true);
    }

    public void RunWithoutPrompt(){
        Main(false);
    }

    private void Main(boolean showPrompt) {
        QuizResultClass mQResultsClass;
        boolean isRetry = false;
        if (showPrompt) {
            mQResultsClass = new QuizResultClass(2, mSessionInfo.mFileNames.length, SessionInfo.SpellingValue, mSessionInfo.mDelay); // arg
        } else {
            mQResultsClass = new QuizResultClass(1, mSessionInfo.mFileNames.length, SessionInfo.SpellingValue, mSessionInfo.mDelay); // arg); // arg
        }
        String user_response;
        String actual_answer;
        QuizResponseDialog dlgQR = new QuizResponseDialog(mSessionInfo.mTaxaLevel, false);
        //Display image with name
        //User types response
        Image temp = ImageScaler.VerifyImageSize(mSessionInfo.mImageCollection.getCurrentImage().getImage(), mSessionInfo.mDisplayLabel.getWidth(), mSessionInfo.mDisplayLabel.getHeight(), mSessionInfo.mDisplayLabel);
        AddNameLabel();
        if (showPrompt) {
            setImageName(getParsedName(mSessionInfo.mImageCollection.getImageName()));
        } else {
            ShowFixation();
        }

        ////////////  INITIALIZE PIC BEFORE SHOWING

        mSessionInfo.mDisplayLabel.setIcon(new ImageIcon(temp));
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
        }

        QuizGrade mGrade = new QuizGrade();
        for (int i = 0; i < mSessionInfo.mFileNames.length; i++) {
            mGrade.BeginQuestion();
            while (Running) {
                SetDlgPos(dlgQR, showPrompt);
                dlgQR.show();
                if (!dlgQR.mRunning) {
                    Running = false;
                    mGrade.QuitEarly();
                    break;
                }
                String completeAnswer = mSessionInfo.mImageCollection.getImageName();
                completeAnswer = AdvancedTrim.Trim(completeAnswer);
                user_response = dlgQR.getResponse().trim();
                actual_answer = getParsedName(completeAnswer.toLowerCase().trim());

                if (SpellingClass.getPassOrFail(SessionInfo.SpellingValue, actual_answer, user_response)) {
                    mGrade.RightAnswer();
                    if (mGrade.getTries() == 1) {
                        mQResultsClass.applyResult(completeAnswer, true);
                    }
                   // SwingUtilities.getWindowAncestor(mSessionInfo.mParentForm);
                    RandomResponse.ShowPositiveResponse(mSessionInfo.mParentForm);
                    break;
                } else {
                    if (mGrade.getTries() == 1) {
                        mQResultsClass.applyResult(completeAnswer, false);
                    }
                    if (mGrade.ShowTryAgainDialog() != JOptionPane.OK_OPTION) {
                        break;
                    } else {
                        mGrade.NextTry();
                        if (!showPrompt) {
                            mSessionInfo.mDisplayLabel.setIcon(null);
                            ShowFixation();
                            mSessionInfo.mDisplayLabel.setIcon(new ImageIcon(temp));
                            mSessionInfo.mDisplayLabel.setVisible(false);
                        }
                    }
                }

            }
            if (!Running || i == mSessionInfo.mFileNames.length - 1) {
                break;
            }
            mSessionInfo.mDisplayLabel.setIcon(null);

            if (!showPrompt) {
                mSessionInfo.mDisplayLabel.setVisible(true);
                ShowFixation();
            } else {
                mNameLabel.setText("");
            }
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
            }

            mSessionInfo.mImageCollection.MoveNext();
            temp = ImageScaler.VerifyImageSize(mSessionInfo.mImageCollection.getCurrentImage().getImage(), mSessionInfo.mDisplayLabel.getWidth(), mSessionInfo.mDisplayLabel.getHeight(), mSessionInfo.mDisplayLabel);
            mSessionInfo.mDisplayLabel.setIcon(new ImageIcon(temp));
            if (showPrompt) {
                setImageName(getParsedName(mSessionInfo.mImageCollection.getImageName()));
            }
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
            }

        }
        mSessionInfo.mDisplayLabel.setIcon(null);
        mSessionInfo.mDisplayLabel.setVisible(true);
        if (mMode == 1) {
            mNameLabel.setVisible(false);
        }

        if (mScriptMode) {
            ScriptResult rs = new ScriptResult("Image Naming", mSessionInfo.mTaxaLevel, mSessionInfo.mTaxa, mSessionInfo.mUserName, mGrade.NumberOfRightAnswers() / (double) mGrade.TotalQuestions(),
                    mSessionInfo.mScriptName, mSessionInfo.mSessionName, mGrade.TotalQuestions(), mSessionInfo.mImageCollection.NumberOfImages(), mSessionInfo.mScriptFileName);
            rs.writeResults();

        } else {
            ScriptResult rs = new ScriptResult("Quiz-Image Naming", mSessionInfo.mTaxaLevel, mSessionInfo.mTaxa, mSessionInfo.mUserName, mGrade.NumberOfRightAnswers() / (double) mGrade.TotalQuestions(),
                    "n/a", "n/a", mGrade.TotalQuestions(), mSessionInfo.mImageCollection.NumberOfImages(), mSessionInfo.mScriptFileName);
            rs.writeResults(false);

        }
        mSessionInfo.mProgress.StoreResult(mQResultsClass, String.valueOf(DecimalFormat.getPercentInstance().format(mGrade.NumberOfRightAnswers() / (double) mSessionInfo.mImageCollection.NumberOfImages())),italic);
    }
    private void AddNameLabel() {
        mNameLabel = new JLabel();
        mNameLabel.setHorizontalAlignment(JLabel.CENTER);
        mNameLabel.setFont(new Font("Tahoma", Font.BOLD, 30));
        xCenter = mSessionInfo.mDisplayLabel.getWidth() / 2;
        mNameLabelHeight = 6 * mNameLabel.getFont().getSize() / 5;
        mNameLabel.setOpaque(true);
        if (mMode == 1) {
            mNameLabelY = (int) (mSessionInfo.mDisplayLabel.getSize().getHeight() / 2) + 200;
        } else if (mMode == 4) {
            mNameLabelY = (int) (mSessionInfo.mDisplayLabel.getSize().getHeight() / 2);
        }
        mSessionInfo.mDisplayLabel.add(mNameLabel);
    }

    public void setImageName(String txt) {
        int width = (int) (3 * mNameLabel.getFont().getSize() / 5 * (txt.length() + 4));
        mNameLabel.setLocation(xCenter - width / 2, mNameLabelY);
        mNameLabel.setSize(width, mNameLabelHeight);
        mNameLabel.setText(txt);
    }

    private void SetDlgPos(QuizResponseDialog dr, boolean showPrompt) {
        if (showPrompt) {
            JCDelay(mSessionInfo.mDelay);
            int dlgX = (mSessionInfo.mParentForm.getWidth() / 2) - 125;
            int dlgY = (mSessionInfo.mParentForm.getHeight() / 4) - 63;
            dr.setLocation(dlgX, dlgY); // Setting location upper center!
        } else {
            mSessionInfo.mDisplayLabel.setVisible(true);
            JCDelay(mSessionInfo.mDelay);
            mSessionInfo.mDisplayLabel.setVisible(false);
            dr.setLocationRelativeTo(mSessionInfo.mParentForm);
        }
    }

    private void ShowFixation() {
        mSessionInfo.mDisplayLabel.setVisible(true);
        mSessionInfo.mDisplayLabel.setText("+");
        JCDelay(mSessionInfo.mFixationTime);
        mSessionInfo.mDisplayLabel.setText("");
    }

    public void paused(boolean pause) {
        mPaused = pause;

    }

    @SuppressWarnings("empty-statement")
    private void JCDelay(double de) {
        de = de * 1000;
        double startTime = System.currentTimeMillis();
        double stopTime = startTime + de;
        while (System.currentTimeMillis() < stopTime && Running) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
            }
        }
        ;
    }

    private String getParsedName(String temp) {
        int index = 0;

        // IF mTlevel = Family Do nothing
        // IF mTlevel = Genus show only Genus
        // If mTlevel = other show all but no Family name

        if (mSessionInfo.mTaxaLevel.compareTo("Family") != 0) {
            index = temp.indexOf(' ');
            temp = temp.substring(index + 1, temp.length());

        }
        return temp;
    }
}
