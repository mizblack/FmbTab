package com.eye3.golfpay.fragment;

import com.eye3.golfpay.model.field.Course;
import com.eye3.golfpay.model.field.Hole;
import com.eye3.golfpay.model.score.Score;
import com.eye3.golfpay.model.teeup.GuestLight;
import com.eye3.golfpay.model.teeup.Player;
import com.eye3.golfpay.model.teeup.Player2;
import com.eye3.golfpay.model.teeup.ScoreReserve;
import com.eye3.golfpay.net.ResponseData;

import java.util.ArrayList;
import java.util.List;

//스코어 api 경량화로 기존 데이터와 호환성을 맞춘다.
public class ScoreConverter {

    private List<Player2> response;
    private ResponseData<Player> score = new ResponseData<>();
    private List<GuestLight> guests = new ArrayList<>();

    public ScoreConverter(List<Player2> responseData) {
        this.response = responseData;
    }

    public ResponseData<Player> adapter() {
        setGuestData();
        score.list = new ArrayList<>();
        int guestCount = getTotalGuestCount();
        for (int i = 0; i < guestCount; i++) {
            Player player = new Player();

            player.guest_id = guests.get(i).id;
            player.bagName = "";
            player.guestName = guests.get(i).guestName;
            player.reserve_id = getLastReserveId(guests.get(i).id);
            player.course = getCourse(i);
            player.totalPar = guests.get(i).totalPar;
            player.totalTar = guests.get(i).totalTar;
            player.totalRankingPutting = guests.get(i).totalPutting;
            player.totalRankingPutting = guests.get(i).totalRankingPutting;
            player.ranking = guests.get(i).ranking;
            player.lastHoleNo = getLastHoleNumber(guests.get(i).id);

            score.getList().add(player);
        }


        return score;
    }

    private void setGuestData() {
        for (ScoreReserve scoreReserve : response.get(0).reserves) {
            guests.addAll(scoreReserve.guest);
        }
    }

    private int getTotalGuestCount() {
        return guests.size();
    }

    private List<Course> getCourse(int guestIndex) {
        List<Course> courses = new ArrayList<>();

        for (int courseIndex = 0; courseIndex < response.size(); courseIndex++) {
            Course course = new Course();
            course.id = response.get(courseIndex).course_id;
            course.courseName = response.get(courseIndex).courseName;
            course.ctype = response.get(courseIndex).ctype;

            course.holes = new ArrayList<>();
            for (int i = 0; i < getHoleCount(courseIndex); i++) {
                Hole hole = new Hole();
                hole.id = getHoleToken(response.get(courseIndex).hole_id, i, null);
                hole.par = getHoleToken(response.get(courseIndex).par, i, null);
                hole.hole_no = getHoleToken(response.get(courseIndex).hole_no, i, null);
                hole.handiCap = getHoleToken(response.get(courseIndex).handiCap, i, null);
                hole.hole_total_size = getHoleToken(response.get(courseIndex).hole_total_size, i, null);
                hole.playedScore = getPlayedScore(guestIndex, i);
                course.holes.add(hole);
            }
            courses.add(course);
        }

        return courses;
    }

    private int getHoleCount(int index) {
        String str = response.get(index).hole_id;
        String[] tokens = str.split(",");
        return tokens.length;
    }

    private String getHoleToken(String tokenizer, int index, String defaultValue) {
        String[] tokens = tokenizer.split(",");

        if (tokens == null || tokens.length == 0 || tokens.length <= index) {
            return defaultValue == null ? "" : defaultValue;
        }

        if (defaultValue != null && tokens[index].isEmpty())
            return defaultValue;

        return tokens[index];
    }

    private Score getPlayedScore(int index, int holeIndex) {
        Score score = new Score();
        score.par = getHoleToken(guests.get(index).par, holeIndex, "-");
        score.tar = getHoleToken(guests.get(index).tar, holeIndex, "-");
        score.putting = getHoleToken(guests.get(index).putting, holeIndex, "-");
        score.teeShot = getHoleToken(guests.get(index).tee_shot, holeIndex, null);

        return score;
    }

    private String getLastReserveId(String guestId) {
        for (ScoreReserve scoreReserve : response.get(0).reserves) {
            for (GuestLight guest : scoreReserve.guest) {
                if (guest.id.equals(guestId)) {
                    return scoreReserve.reserve_id;
                }
            }
        }

        return "";
    }

    private int getLastHoleNumber(String guestId) {
        for (Player2 player2 : response) {
            for (ScoreReserve scoreReserve : player2.reserves) {
                for (GuestLight guest : scoreReserve.guest) {
                    if (guest.id.equals(guestId)) {
                        return response.get(0).lastHoleNo;
                    }
                }
            }
        }

        return 0;
    }
}
