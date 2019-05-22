package com.theopus.xengine.core;

import static com.theopus.xengine.core.XEngine.MAIN_CAMERA;

import java.util.HashMap;
import java.util.Map;

import org.joml.Vector3f;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.theopus.xengine.core.ecs.components.Camera;
import com.theopus.xengine.core.ecs.components.Light;
import com.theopus.xengine.core.ecs.components.Render;
import com.theopus.xengine.core.ecs.components.Transformation;
import com.theopus.xengine.core.ecs.components.TransformationMatrix;
import com.theopus.xengine.core.ecs.components.Velocity;
import com.theopus.xengine.core.render.ArtemisRenderModule;
import com.theopus.xengine.core.render.BaseRenderer;
import com.theopus.xengine.core.render.RenderModule;

public class EntityFactory {
    private static final ArchetypeBuilder lightArchetype = new ArchetypeBuilder().add(Light.class, Transformation.class);

    @Wire
    private World world;
    @Wire
    private TagManager tagManager;
    @Wire
    private ComponentMapper<Render> render;
    @Wire
    private ComponentMapper<Light> light;
    @Wire
    private ComponentMapper<Transformation> transformation;

    @Wire(name = "renderer")
    private BaseRenderer renderer;

    private Map<Class<? extends RenderModule>, Archetype> archetypes;

    public EntityFactory() {
        archetypes = new HashMap<>();
    }

    public void init() {
        for (Map.Entry<Class<RenderModule<?>>, RenderModule<?>> classRenderModuleEntry : renderer.moduleMap().entrySet()) {
            RenderModule<?> module = classRenderModuleEntry.getValue();
            Class<RenderModule<?>> key = classRenderModuleEntry.getKey();
            if (module instanceof ArtemisRenderModule) {
                ArchetypeBuilder components = ((ArtemisRenderModule) module).components();
                components.add(Transformation.class);
                components.add(Render.class);
                components.add(Velocity.class);
                System.out.println(key);
                Archetype ar = components.build(world);
                archetypes.put(key, ar);
            }
        }
    }

    public int createFor(Class<? extends RenderModule> module) {
        int entity = world.create(archetypes.get(module));
        return entity;
    }

    public int createFor(Class<? extends RenderModule> module, Vector3f position) {
        return createFor(module, position, (String) renderer.get(module).models().stream().findFirst().get());
    }

    public int createFor(Class<? extends RenderModule> module, Vector3f position, String model) {
        RenderModule renderModule = renderer.get(module);

        int entity = world.create(archetypes.get(module));

        Render render = this.render.get(entity);
        render.renderable = true;
        render.renderer = module;
        render.model = model;

        Transformation transformation = this.transformation.get(entity);
        transformation.position.set(position);

        renderModule.bind(render.model, entity);
        return entity;
    }

    public int createLight(Vector3f color, Vector3f position) {
        int i = world.create(lightArchetype.build(world));
        Light light = this.light.get(i);
        light.diffuse.x = color.x;
        light.diffuse.y = color.y;
        light.diffuse.z = color.z;

        placeAt(i, position);
        return i;
    }

    public int createCamera() {
        int cameraAcnhor = world.create();
        world.getMapper(Transformation.class).create(cameraAcnhor);
        world.getMapper(TransformationMatrix.class).create(cameraAcnhor);
        world.getMapper(Velocity.class).create(cameraAcnhor);
        Archetype cameraArchetype = new ArchetypeBuilder().add(Camera.class, Transformation.class).build(world);

        int id = world.create(cameraArchetype);
        Camera camera = world.getMapper(Camera.class).get(id);
        camera.target = cameraAcnhor;
        tagManager.register(MAIN_CAMERA, id);

        return cameraAcnhor;
    }

    public void placeAt(int entity, float x, float y, float z) {
        placeAt(entity, new Vector3f(x, y, z));
    }

    public void placeAt(int entity, Vector3f position) {
        Transformation p = transformation.get(entity);
        p.position.x = position.x;
        p.position.y = position.y;
        p.position.z = position.z;
    }
}
