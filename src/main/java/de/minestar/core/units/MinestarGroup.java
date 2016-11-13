/*
 * Copyright (C) 2012 MineStar.de 
 * 
 * This file is part of MinestarCore.
 * 
 * MinestarCore is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * MinestarCore is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MinestarCore.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.minestar.core.units;

public enum MinestarGroup {
    //@formatter:off
    ADMIN   ("admins",  50),
    MOD     ("mods",    40),
    PAY     ("pay",     30),
    FREE    ("vip",     20),
    PROBE   ("probe",   10),
    DEFAULT ("default", 0),
    X       ("X",       -10);
    //@formatter:on

    // The groupname
    private String name;
    // The level of this group: high levels are groups with more permissions
    private int level;

    private MinestarGroup(String name, int level) {
        this.name = name;
        this.level = level;
    }

    /** @return The GroupManager group name as defined in the group.yml */
    public String getName() {
        return name;
    }
    
    public String getShort() {
        switch (name) {
            case "admins": return "[A]";
            case "mods": return "[M]";
            case "pay": return "[P]";
            case "vip": return "[F]";
            case "probe": return "[T]";
            default: return "[D]";
        }
    }

    public static MinestarGroup getGroup(String groupName) {
        for (MinestarGroup group : MinestarGroup.values())
            if (group.getName().equalsIgnoreCase(groupName))
                return group;
        return MinestarGroup.DEFAULT;
    }

    public int getLevel() {
        return this.level;
    }

    public boolean isGroupHigher(MinestarGroup otherGroup) {
        if (otherGroup == null)
            return false;
        return this.level > otherGroup.getLevel();
    }
}
