package com.mau.chorely.activities.centralFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mau.chorely.R;
import com.mau.chorely.activities.centralFragments.utils.CentralActivityRecyclerViewAdapter;
import com.mau.chorely.activities.centralFragments.utils.ListItemCentral;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import shared.transferable.Group;
import shared.transferable.User;

/**
 * Fragment containing the scoreboard.
 * @author Timothy Denison, Angelica Asplund.
 */
public class FragmentScore extends Fragment {
    private static HashMap<User, Integer> scoreMap;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private static ArrayList<ListItemCentral> leaderBoard = new ArrayList<>();
    private static CentralActivityRecyclerViewAdapter adapter = new CentralActivityRecyclerViewAdapter(leaderBoard);

    public static Fragment newInstance(HashMap<User, Integer> scoreMap, Group selectedGroup) {
        FragmentScore fragment = new FragmentScore();
        Bundle args = new Bundle();
        args.putSerializable("SCORE", scoreMap);
        args.putSerializable("SELECTED GROUP", selectedGroup);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        leaderBoard = new ArrayList<>();
        if (getArguments() != null) {
            scoreMap = (HashMap<User, Integer>) getArguments().getSerializable("SCORE");

        }
        initList();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_score, container, false);
        recyclerView = view.findViewById(R.id.fragment_score_recyclerView);
        buildRecyclerView();

        adapter.notifyDataSetChanged();
        return view;


    }


    public void buildRecyclerView() {
        recyclerView.hasFixedSize();
        layoutManager = new LinearLayoutManager(getActivity());
        adapter = new CentralActivityRecyclerViewAdapter(leaderBoard, R.layout.scoreboard_item);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    /**
     * Method to validate and update the list of scores in the group.
     *
     * @param newMap new hashmap of scores and users.
     */
    public static void updateList(HashMap<User, Integer> newMap) {
        System.out.println("UPDATING SCOREBOARD OUTSIDE IF");
        scoreMap = newMap;
        if (adapter != null) {

            System.out.println("UPDATING SCOREBOARD INSIDE IF");
            boolean updated = false;
            boolean foundEntry = false;
            int size = leaderBoard.size();
            for (Map.Entry<User, Integer> entry : scoreMap.entrySet()) {
                for (int i = 0; i < size; i++) {
                    System.out.println(leaderBoard.get(i).getTitle() + " ?= " + entry.getKey().getUsername());
                    if (leaderBoard.get(i).getTitle().equals(entry.getKey().getUsername())) {
                        System.out.println(leaderBoard.get(i).getPointsInt() + " ?= " + entry.getValue());
                        if (leaderBoard.get(i).getPointsInt() != entry.getValue()) {
                            System.out.println("UPDATING SCORE!!!!");
                            leaderBoard.remove(i);
                            leaderBoard.add(new ListItemCentral(entry.getKey().getUsername(), null, entry.getValue()));
                            updated = true;
                        }
                        foundEntry = true;
                        break;
                    }
                }
                if (!foundEntry) {
                    leaderBoard.add(new ListItemCentral(entry.getKey().getUsername(), null, entry.getValue()));
                    updated = true;
                }
            }

            if (scoreMap.size() > leaderBoard.size()) {
                for (int i = 0; i < leaderBoard.size(); i++) {
                    foundEntry = false;
                    for (Map.Entry<User, Integer> entry : scoreMap.entrySet()) {
                        if (entry.getKey().getUsername().equals(leaderBoard.get(i).getTitle())) {
                            foundEntry = true;
                        }
                    }
                    if (!foundEntry) {
                        leaderBoard.remove(i);
                    }
                }
            }

            if (updated) {
                adapter.notifyDataSetChanged();

            }
            System.out.println("UPDATED SCOREBOARD: Size: " + leaderBoard.size());
            adapter.notifyDataSetChanged();
        }
        sortLeaderboard();
    }

    /**
     * Method to initiate the list.
     */
    private static void initList() {

        for (Map.Entry<User, Integer> entry : scoreMap.entrySet()) {

            User user = entry.getKey();
            int points = entry.getValue();
            System.out.println("ADDED VALUES TO SCOREBOARD: Name:" + user.getUsername() + " Points: " + points);
            ListItemCentral item = new ListItemCentral(user.getUsername(), "", points);
            leaderBoard.add(item);
        }
        sortLeaderboard();
    }
    private static void sortLeaderboard () {
        Collections.sort(leaderBoard, new Comparator<ListItemCentral>() {
            @Override
            public int compare(ListItemCentral o1, ListItemCentral o2) {
                return o1.getPointsInt() - o2.getPointsInt();
            }
        });
    }

}
