package net.pixaurora.kit_tunes.build_logic.mod_resources_gen.extension;

import java.nio.file.Path;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaBasePlugin;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Nested;

import net.fabricmc.loom.LoomGradleExtension;
import net.fabricmc.loom.api.LoomGradleExtensionAPI;

public abstract class ModInfoExtension {
    private final Project project;

    public ModInfoExtension(Project project) {
        this.project = project;
    }

    @Input
    public abstract Property<String> getId();

    @Input
    public abstract Property<String> getVersion();

    @Nested
    public abstract ModMetadata getMetadata();

    @Nested
    public abstract ModDependencies getDependencies();

    @Nested
    public abstract Entrypoints getEntrypoints();

    @Input
    public abstract Property<String> getIntermediaryMappings();

    @Input
    public abstract Property<String> getMixinFile();

    @Input
    public abstract Property<String> getAccessWidener();

    public void mixin(String path) {
        var mixinFile = Path.of(path);
        this.getMixinFile().set(mixinFile.toString());
    }

    public void accessWidener(String path) {
        var loom = this.project.getExtensions().getByType(LoomGradleExtensionAPI.class);

        var projectDir = project.getProjectDir().toPath();
        var accessWidenerPath = projectDir.resolve(Path.of("src", "main", "resources", path));

        loom.getAccessWidenerPath().set(accessWidenerPath.toFile());

        this.getAccessWidener().set(path);
    }

    public void metadata(Action<? super ModMetadata> configuration) {
        configuration.execute(this.getMetadata());
    }

    public void dependencies(Action<? super ModDependencies> configuration) {
        configuration.execute(this.getDependencies());
    }

    public void entrypoint(String name, String classPath) {
        this.getEntrypoints().create(name, classPath);
    }
}
