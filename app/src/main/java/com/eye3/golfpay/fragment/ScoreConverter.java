package com.eye3.golfpay.fragment;

import com.eye3.golfpay.model.field.Course;
import com.eye3.golfpay.model.field.Hole;
import com.eye3.golfpay.model.score.Score;
import com.eye3.golfpay.model.teeup.GuestLite;
import com.eye3.golfpay.model.teeup.LiteCourse;
import com.eye3.golfpay.model.teeup.Player;
import com.eye3.golfpay.model.teeup.LiteScore;
import com.eye3.golfpay.model.teeup.ScoreReserve;
import com.eye3.golfpay.net.ResponseData;

import java.util.ArrayList;
import java.util.List;

//스코어 api 경량화로 기존 데이터와 호환성을 맞춘다.
public class ScoreConverter {

    private LiteScore response;
    private ResponseData<Player> score = new ResponseData<>();
    private List<GuestLite> guests = new ArrayList<>();

    public ScoreConverter(LiteScore responseData) {
        this.response = responseData;
    }

    public ResponseData<Player> adapter() {

        score.list = new ArrayList<>();
        //전반 후반
        for (int reserveIndex = 0; reserveIndex < response.reserves.size(); reserveIndex++) {
            int guestCount = response.reserves.get(reserveIndex).guest.size();
            guests.clear();
            guests.addAll(response.reserves.get(reserveIndex).guest);

            for (int i = 0; i < guestCount; i++) {
                Player player = new Player();
                player.guest_id = guests.get(i).id;
                player.bagName = "";
                player.guestName = guests.get(i).guestName;
                player.reserve_id = response.reserves.get(reserveIndex).reserve_id;
                player.course = getCourse(reserveIndex, i);
                player.totalPar = guests.get(i).totalPar;
                player.totalTar = guests.get(i).totalTar;
                player.totalRankingPutting = guests.get(i).totalPutting;
                player.totalRankingPutting = guests.get(i).totalRankingPutting;
                player.ranking = guests.get(i).ranking;
                player.lastHoleNo = response.reserves.get(reserveIndex).lastHoleNo;

                score.getList().add(player);
            }
        }

        return score;
    }

    private List<Course> getCourse(int reserveIndex, int guestIndex) {
        List<Course> courses = new ArrayList<>();

        String reserveId = response.reserves.get(reserveIndex).reserve_id;
        String course_a_id = response.reserves.get(reserveIndex).course_a_id;//전반 코스 Id
        String course_b_id = response.reserves.get(reserveIndex).course_b_id;//후반 코스 Id

        //전반
        LiteCourse liteCourse = findLiteCourse(course_a_id);
        Course course = new Course();
        course.id = course_a_id;
        course.courseName = liteCourse.courseName;
        course.ctype = liteCourse.ctype;
        course.holes = getHoles(0, guestIndex, liteCourse);
        courses.add(course);

        //후반
        liteCourse = findLiteCourse(course_b_id);
        course = new Course();
        course.id = course_b_id;
        course.courseName = liteCourse.courseName;
        course.ctype = liteCourse.ctype;
        course.holes = getHoles(1, guestIndex, liteCourse);
        courses.add(course);

        return courses;
    }

    private LiteCourse findLiteCourse(String id) {
        for (LiteCourse liteCourse : response.course) {
            if (liteCourse.course_id.equals(id))
                return liteCourse;
        }

        return null;
    }

    private List<Hole> getHoles(int gameIndex, int guestIndex, LiteCourse liteCourse) {
        List<Hole> holes = new ArrayList<>();
        for (int i = 0; i < getHoleCount(liteCourse); i++) {
            Hole hole = new Hole();
            hole.id = getHoleToken(liteCourse.hole_id, i, null);
            hole.par = getHoleToken(liteCourse.par, i, null);
            hole.hole_no = getHoleToken(liteCourse.hole_no, i, null);
            hole.handiCap = getHoleToken(liteCourse.handiCap, i, null);
            hole.hole_total_size = getHoleToken(liteCourse.hole_total_size, i, null);
            hole.playedScore = getPlayedScore(gameIndex, guestIndex, i);
            holes.add(hole);
        }

        return holes;
    }

    private int getHoleCount(LiteCourse course) {
        String str = course.hole_id;
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

    private Score getPlayedScore(int gameIndex/*전반후반*/, int guestIndex, int holeIndex) {
        Score score = new Score();
        if (gameIndex == 0) {
            score.par = getHoleToken(guests.get(guestIndex).par_a, holeIndex, "-");
            score.tar = getHoleToken(guests.get(guestIndex).tar_a, holeIndex, "-");
            score.putting = getHoleToken(guests.get(guestIndex).putting_a, holeIndex, "-");
            score.teeShot = getHoleToken(guests.get(guestIndex).tee_shot_a, holeIndex, null);
        } else  {
            score.par = getHoleToken(guests.get(guestIndex).par_b, holeIndex, "-");
            score.tar = getHoleToken(guests.get(guestIndex).tar_b, holeIndex, "-");
            score.putting = getHoleToken(guests.get(guestIndex).putting_b, holeIndex, "-");
            score.teeShot = getHoleToken(guests.get(guestIndex).tee_shot_b, holeIndex, null);
        }

        return score;
    }
}
