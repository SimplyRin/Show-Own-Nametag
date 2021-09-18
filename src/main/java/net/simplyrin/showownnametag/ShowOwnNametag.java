package net.simplyrin.showownnametag;

import lombok.Data;
import lombok.Getter;
import net.fabricmc.api.ModInitializer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Getter
public class ShowOwnNametag implements ModInitializer {

    @Getter
    private static ShowOwnNametag instance;

    private File config;
    private boolean toggle;

    @Override
    public void onInitialize() {
        instance = this;

        File mod = new File(new File("config"), "ShowOwnNametag");
        mod.mkdirs();

        this.config = new File(mod, "config.txt");
        if (!this.config.exists()) {
            try {
                this.config.createNewFile();
                this.setStatus(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.loadMod();
    }

    public void setStatus(boolean bool) {
        this.toggle = bool;
        this.saveMod();
    }

    public void loadMod() {
        Path path = Paths.get(this.config.toURI());
        try {
            String value = Files.readString(path);
            this.toggle = Boolean.valueOf(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveMod() {
        try {
            FileWriter fw = new FileWriter(this.config);
            fw.write(String.valueOf(this.toggle));
            fw.close();
        } catch(IOException e) {
            System.out.println(e);
        }
    }

}
