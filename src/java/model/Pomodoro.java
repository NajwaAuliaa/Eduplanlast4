/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ASUS
 */

public class Pomodoro extends LearningMode {
    private int studyTime; // In minutes

    public Pomodoro(int studyTime) {
        this.studyTime = studyTime;
    }

    public int getStudyTime() {
        return studyTime;
    }

    public void setStudyTime(int studyTime) {
        this.studyTime = studyTime;
    }

    @Override
    public String getModeDescription() {
        return "Mode Pomodoro untuk fokus pada tugas dengan istirahat berkala.";
    }
}
