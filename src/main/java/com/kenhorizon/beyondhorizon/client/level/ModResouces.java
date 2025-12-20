package com.kenhorizon.beyondhorizon.client.level;

import net.minecraftforge.forgespi.locating.IModFile;
import net.minecraftforge.resource.PathPackResources;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public final class ModResouces extends PathPackResources {
    private final IModFile file;
    private final String sourcePath;
    public ModResouces(String name, IModFile file, String sourcePath) {
        super(name, true, file.findResource(sourcePath));
        this.file = file;
        this.sourcePath = sourcePath;
    }
    @Override
    protected @NotNull Path resolve(String... paths) {
        String[] allPaths = new String[paths.length + 1];
        allPaths[0] = sourcePath;
        System.arraycopy(paths, 0, allPaths, 1, paths.length);
        return this.file.findResource(allPaths);
    }
}
