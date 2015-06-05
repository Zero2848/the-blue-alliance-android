package com.thebluealliance.androidclient.datafeed;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.thebluealliance.androidclient.models.Award;
import com.thebluealliance.androidclient.models.District;
import com.thebluealliance.androidclient.models.Event;
import com.thebluealliance.androidclient.models.Match;
import com.thebluealliance.androidclient.models.Media;
import com.thebluealliance.androidclient.models.Team;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Path;
import rx.Observable;

/**
 * Interface for TBA API spec to be used with Retrofit
 */
interface APIv2 {

    @GET("/teams/{pageNum}")
    Observable<List<Team>> fetchTeamPage(
            @Path("pageNum") int pageNum,
            @Header("If-Modified-Since") String ifModifiedSince
    );

    @GET("/team/{teamKey}")
    Observable<Team> fetchTeam(
            @Path("teamKey") String teamKey,
            @Header("If-Modified-Since") String ifModifiedSince
    );

    @GET("/team/{teamKey}/{year}/events")
    Observable<List<Event>> fetchTeamEvents(
            @Path("teamKey") String teamKey,
            @Path("year") int year,
            @Header("If-Modified-Since") String ifModifiedSince
    );

    @GET("/team/{teamKey}/event/{eventKey}/awards")
    Observable<List<Award>> fetchTeamAtEventAwards(
            @Path("teamKey") String teamKey,
            @Path("eventKey") String eventKey,
            @Header("If-Modified-Since") String ifModifiedSince
    );

    @GET("/team/{teamKey}/event/{eventKey}/matches")
    Observable<List<Match>> fetchTeamAtEventMatches(
            @Path("teamKey") String teamKey,
            @Path("eventKey") String eventKey,
            @Header("If-Modified-Since") String ifModifiedSince
    );

    @GET("/team/{teamKey}/years_participated")
    Observable<List<Integer>> fetchTeamYearsParticipated(
            @Path("teamKey") String teamKey,
            @Header("If-Modified-Since") String ifModifiedSince
    );

    @GET("/team/{teamKey}/{year}/media")
    Observable<List<Media>> fetchTeamMediaInYear(
            @Path("teamKey") String teamKey,
            @Path("year") int year,
            @Header("If-Modified-Since") String ifModifiedSince
    );

    @GET("/team/{teamKey}/history/events")
    Observable<List<Event>> fetchTeamEventHistory(
            @Path("teamKey") String teamKey,
            @Header("If-Modified-Since") String ifModifiedSince
    );

    @GET("/team/{teamKey}/history/awards")
    Observable<List<Award>> fetchTeamEventAwards(
            @Path("teamKey") String teamKey,
            @Header("If-Modified-Since") String ifModifiedSince
    );

    @GET("/events/{year}")
    Observable<List<Event>> fetchEventsInYear(
            @Path("year") int year,
            @Header("If-Modified-Since") String ifModifiedSince
    );

    @GET("/event/{eventKey}")
    Observable<Event> fetchEvent(
            @Path("eventKey") String eventKey,
            @Header("If-Modified-Since") String ifModifiedSince
    );

    @GET("/event/{eventKey}/teams")
    Observable<List<Team>> fetchEventTeams(
            @Path("eventKey") String eventKey,
            @Header("If-Modified-Since") String ifModifiedSince
    );

    @GET("/event/{eventKey}/rankings")
    Observable<JsonArray> fetchEventRankings(
            @Path("eventKey") String eventKey,
            @Header("If-Modified-Since") String ifModifiedSince
    );

    @GET("/event/{eventKey}/matches")
    Observable<List<Match>> fetchEventMatches(
            @Path("eventKey") String eventKey,
            @Header("If-Modified-Since") String ifModifiedSince
    );

    @GET("/event/{eventKey}/stats")
    Observable<JsonObject> fetchEventStats(
            @Path("eventKey") String eventKey,
            @Header("If-Modified-Since") String ifModifiedSince
    );

    @GET("/event/{eventKey}/awards")
    Observable<List<Award>> fetchEventAwards(
            @Path("eventKey") String eventKey,
            @Header("If-Modified-Since") String ifModifiedSince
    );

    @GET("/event/{eventKey}/district_points")
    Observable<JsonObject> fetchEventDistrictPoints(
            @Path("eventKey") String eventKey,
            @Header("If-Modified-Since") String ifModifiedSince
    );

    @GET("/districts/{year}")
    Observable<List<District>> fetchDistrictList(
            @Path("year") int year,
            @Header("If-Modified-Since") String ifModifiedSince
    );

    @GET("/district/{districtShort}/{year}/events")
    Observable<List<Event>> fetchDistrictEvents(
            @Path("districtShort") String districtShort,
            @Path("year") int year,
            @Header("If-Modified-Since") String ifModifiedSince
    );

    @GET("/district/{districtShort}/{year}/rankings")
    Observable<JsonArray> fetchDistrictRankings(
            @Path("districtShort") String districtShort,
            @Path("year") int year,
            @Header("If-Modified-Since") String ifModifiedSince
    );

    @GET("/match/{matchKey}")
    Observable<Match> fetchMatch(
            @Path("matchKey") String matchKey,
            @Header("If-Modified-Since") String ifModifiedSince
    );
}