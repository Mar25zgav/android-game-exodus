package com.example.exodus;

import com.example.exodus.gameobject.Arena;
import com.example.exodus.gameobject.Chest;
import com.example.exodus.gameobject.Enemy;
import com.example.exodus.gameobject.Player;
import com.example.exodus.menupanel.GameActivity;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;

public class PVector implements Serializable {
    public float x;
    public float y;
    private float z;
    private transient float[] array;

    private PVector() {
    }

    private PVector(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public PVector(float x, float y) {
        this.x = x;
        this.y = y;
        this.z = 0;
    }

    public static PVector getRandomEnemyPos(Player player, List<Enemy> enemyList) {
        int i;
        PVector position = new PVector();
        do{
            i = 0;
            position.randomEnemyPVector();
            while (position.dist(player.getPVector()) < player.getRadius() * 10) {
                position.randomEnemyPVector();
            }
            for(Enemy enemy : enemyList){
                if (position.dist(enemy.getPVector()) > enemy.getRadius() * 2)
                    i++;
            }
        }while(i < enemyList.size());
        return position;
    }

    public static PVector getRandomChestPos(Player player, List<Enemy> enemyList, List<Chest> chestList) {
        int i;
        PVector position = new PVector();
        do{
            i = 0;
            position.randomChestPVector();
            while (position.dist(player.getPVector()) < player.getRadius() * 9) {
                position.randomChestPVector();
            }
            for(Enemy enemy : enemyList) {
                if (position.dist(enemy.getPVector()) > enemy.getRadius() * 4)
                    i++;
            }
            for(Chest chest : chestList) {
                if (position.dist(chest.getPVector()) > Chest.getSize() * 2)
                    i++;
            }
        }while(i < enemyList.size() + chestList.size());
        return position;
    }

    private PVector randomEnemyPVector() {
        RandomInRanges rir = new RandomInRanges(Arena.getWallSize() + LevelManager.getEnemyRadius(), GameActivity.getScreenWidth() - Arena.getWallSize() - LevelManager.getEnemyRadius());
        x = rir.getRandom();
        rir = new RandomInRanges(Arena.getWallSize() + LevelManager.getEnemyRadius(), GameActivity.getScreenHeight() - Arena.getWallSize() - LevelManager.getEnemyRadius());
        y = rir.getRandom();
        return this;
    }

    private PVector randomChestPVector() {
        RandomInRanges rir = new RandomInRanges(Arena.getWallSize(), GameActivity.getScreenWidth() - Arena.getWallSize() - Chest.getSize());
        x = rir.getRandom();
        rir = new RandomInRanges(Arena.getWallSize(), GameActivity.getScreenHeight() - Arena.getWallSize() - Chest.getSize());
        y = rir.getRandom();
        return this;
    }

    public PVector set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public PVector set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public PVector set(PVector v) {
        x = v.x;
        y = v.y;
        z = v.z;
        return this;
    }

    public PVector set(float[] source) {
        if (source.length >= 2) {
            x = source[0];
            y = source[1];
        }
        if (source.length >= 3) {
            z = source[2];
        }
        return this;
    }

    static public PVector fromAngle(float angle) {
        return fromAngle(angle,null);
    }

    private static PVector fromAngle(float angle, PVector target) {
        if (target == null) {
            target = new PVector((float)Math.cos(angle),(float)Math.sin(angle),0);
        } else {
            target.set((float)Math.cos(angle),(float)Math.sin(angle),0);
        }
        return target;
    }

    private PVector copy() {
        return new PVector(x, y, z);
    }


    @Deprecated
    public PVector get() {
        return copy();
    }

    public float[] get(float[] target) {
        if (target == null) {
            return new float[] { x, y, z };
        }
        if (target.length >= 2) {
            target[0] = x;
            target[1] = y;
        }
        if (target.length >= 3) {
            target[2] = z;
        }
        return target;
    }

    private float mag() {
        return (float) Math.sqrt(x*x + y*y + z*z);
    }

    private float magSq() {
        return (x*x + y*y + z*z);
    }

    public PVector add(PVector v) {
        x += v.x;
        y += v.y;
        z += v.z;
        return this;
    }

    public PVector add(float x, float y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public PVector add(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    static public PVector add(PVector v1, PVector v2) {
        return add(v1, v2, null);
    }

    static public PVector add(PVector v1, PVector v2, PVector target) {
        if (target == null) {
            target = new PVector(v1.x + v2.x,v1.y + v2.y, v1.z + v2.z);
        } else {
            target.set(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
        }
        return target;
    }

    public PVector sub(PVector v) {
        x -= v.x;
        y -= v.y;
        z -= v.z;
        return this;
    }

    public PVector sub(float x, float y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public PVector sub(float x, float y, float z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    static public PVector sub(PVector v1, PVector v2) {
        return sub(v1, v2, null);
    }

    private static PVector sub(PVector v1, PVector v2, PVector target) {
        if (target == null) {
            target = new PVector(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
        } else {
            target.set(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
        }
        return target;
    }

    public PVector mult(float n) {
        x *= n;
        y *= n;
        z *= n;
        return this;
    }

    static public PVector mult(PVector v, float n) {
        return mult(v, n, null);
    }

    private static PVector mult(PVector v, float n, PVector target) {
        if (target == null) {
            target = new PVector(v.x*n, v.y*n, v.z*n);
        } else {
            target.set(v.x*n, v.y*n, v.z*n);
        }
        return target;
    }

    public PVector div(float n) {
        x /= n;
        y /= n;
        z /= n;
        return this;
    }

    static public PVector div(PVector v, float n) {
        return div(v, n, null);
    }

    private static PVector div(PVector v, float n, PVector target) {
        if (target == null) {
            target = new PVector(v.x/n, v.y/n, v.z/n);
        } else {
            target.set(v.x/n, v.y/n, v.z/n);
        }
        return target;
    }

    private float dist(PVector v) {
        float dx = x - v.x;
        float dy = y - v.y;
        float dz = z - v.z;
        return (float) Math.sqrt(dx*dx + dy*dy + dz*dz);
    }

    static public float dist(PVector v1, PVector v2) {
        float dx = v1.x - v2.x;
        float dy = v1.y - v2.y;
        float dz = v1.z - v2.z;
        return (float) Math.sqrt(dx*dx + dy*dy + dz*dz);
    }

    public float dot(PVector v) {
        return x*v.x + y*v.y + z*v.z;
    }

    public float dot(float x, float y, float z) {
        return this.x*x + this.y*y + this.z*z;
    }

    static public float dot(PVector v1, PVector v2) {
        return v1.x*v2.x + v1.y*v2.y + v1.z*v2.z;
    }

    public PVector cross(PVector v) {
        return cross(v, null);
    }

    private PVector cross(PVector v, PVector target) {
        float crossX = y * v.z - v.y * z;
        float crossY = z * v.x - v.z * x;
        float crossZ = x * v.y - v.x * y;

        if (target == null) {
            target = new PVector(crossX, crossY, crossZ);
        } else {
            target.set(crossX, crossY, crossZ);
        }
        return target;
    }

    static public PVector cross(PVector v1, PVector v2, PVector target) {
        float crossX = v1.y * v2.z - v2.y * v1.z;
        float crossY = v1.z * v2.x - v2.z * v1.x;
        float crossZ = v1.x * v2.y - v2.x * v1.y;

        if (target == null) {
            target = new PVector(crossX, crossY, crossZ);
        } else {
            target.set(crossX, crossY, crossZ);
        }
        return target;
    }

    public PVector normalize() {
        float m = mag();
        if (m != 0 && m != 1) {
            div(m);
        }
        return this;
    }

    private PVector normalize(PVector target) {
        if (target == null) {
            target = new PVector();
        }
        float m = mag();
        if (m > 0) {
            target.set(x/m, y/m, z/m);
        } else {
            target.set(x, y, z);
        }
        return target;
    }

    public PVector limit(float max) {
        if (magSq() > max*max) {
            normalize();
            mult(max);
        }
        return this;
    }

    public PVector setMag(float len) {
        normalize();
        mult(len);
        return this;
    }

    public PVector setMag(PVector target, float len) {
        target = normalize(target);
        target.mult(len);
        return target;
    }

    private float heading() {
        float angle = (float) Math.atan2(y, x);
        return angle;
    }


    @Deprecated
    public float heading2D() {
        return heading();
    }

    static public float angleBetween(PVector v1, PVector v2) {

        // We get NaN if we pass in a zero vector which can cause problems
        // Zero seems like a reasonable angle between a (0,0,0) vector and something else
        if (v1.x == 0 && v1.y == 0 && v1.z == 0 ) return 0.0f;
        if (v2.x == 0 && v2.y == 0 && v2.z == 0 ) return 0.0f;

        double dot = v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
        double v1mag = Math.sqrt(v1.x * v1.x + v1.y * v1.y + v1.z * v1.z);
        double v2mag = Math.sqrt(v2.x * v2.x + v2.y * v2.y + v2.z * v2.z);
        // This should be a number between -1 and 1, since it's "normalized"
        double amt = dot / (v1mag * v2mag);
        // But if it's not due to rounding error, then we need to fix it
        // http://code.google.com/p/processing/issues/detail?id=340
        // Otherwise if outside the range, acos() will return NaN
        // http://www.cppreference.com/wiki/c/math/acos
        if (amt <= -1) {
            return (float)Math.PI;
        } else if (amt >= 1) {
            // http://code.google.com/p/processing/issues/detail?id=435
            return 0;
        }
        return (float) Math.acos(amt);
    }

    @Override
    public String toString() {
        return "[ " + x + ", " + y + ", " + z + " ]";
    }

    public float[] array() {
        if (array == null) {
            array = new float[3];
        }
        array[0] = x;
        array[1] = y;
        array[2] = z;
        return array;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PVector)) {
            return false;
        }
        final PVector p = (PVector) obj;
        return x == p.x && y == p.y && z == p.z;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + Float.floatToIntBits(x);
        result = 31 * result + Float.floatToIntBits(y);
        result = 31 * result + Float.floatToIntBits(z);
        return result;
    }
}
