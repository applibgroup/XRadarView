package com.orzangleli.radarview;

import com.orzangleli.radarview.slice.DemoAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class DemoAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(DemoAbilitySlice.class.getName());
    }
}
