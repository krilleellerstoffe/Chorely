package com.mau.chorely.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.view.View;
import android.widget.EditText;

import com.mau.chorely.R;
import com.mau.chorely.activities.CreateEditGroupActivity;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import shared.transferable.Group;
import shared.transferable.User;

public class CreateEditGroupActivityTest {
    CreateEditGroupActivity createEditGroupActivity = new CreateEditGroupActivity();

    @Test
    public void saveGroup() {
    }

    @Test
    public void removeMemberFromGroup() {
    }

    @Test
    public void promoteUser() {
    }

    @Test
    public void searchForMember() {
        View view = mock(View.class);
        EditText editText = mock(EditText.class);
        when(view.findViewById(R.id.edit_group_memberSearchText)).thenReturn(editText);
        when(editText.getText().toString()).thenReturn("test");
        when(view.findViewById(R.id.edit_group_memberSearchText)).thenReturn(editText);

        Group selectedGroup = mock(Group.class);
        when(selectedGroup.getUsers()).thenReturn(new ArrayList<>(Arrays.asList(new User("test"))));
        createEditGroupActivity.setSelectedGroup(selectedGroup);
        String result = createEditGroupActivity.searchForMember(view);

        assertEquals("User found but already in the group", result);

    }

    @Test
    public void cancelFoundMember() {
    }

    @Test
    public void addMember() {
    }

    @Test
    public void memberAddedNotification() {
    }
}