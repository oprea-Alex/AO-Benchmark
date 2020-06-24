package ro.ao.benchmark.model.benchmark.real_apps_testing;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.Objects;

public class ItemAppInfo implements Serializable {

    private String name;
    private String packageName;
    private long size;

    public ItemAppInfo() {
    }

    public ItemAppInfo(String packageName, String name, long size) {
        this.packageName = packageName;
        this.name = name;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @NonNull
    @Override
    public String toString() {
        return "{name=" + name + ", packageName=" + packageName + ", size=" + size + "}";
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof ItemAppInfo))
            return false;
        ItemAppInfo info = (ItemAppInfo) obj;
        return  info.name.equals(name) && info.packageName.equals(packageName) && info.size == size;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, packageName, size);
    }
}
