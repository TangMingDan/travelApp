package com.example.travelapp.core.delegates;

public abstract class TravelDelegate extends PermissionCheckrDelegate{
    public <T extends TravelDelegate> T getParentDelegate(){
        return (T) getParentFragment();
    }
}
