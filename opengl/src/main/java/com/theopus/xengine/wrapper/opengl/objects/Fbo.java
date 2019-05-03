package com.theopus.xengine.wrapper.opengl.objects;

import org.lwjgl.opengl.GL30;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.theopus.xengine.wrapper.opengl.MemoryContext;

public class Fbo {
    private static final Logger LOGGER = LoggerFactory.getLogger(Fbo.class);

    private final int id;
    private Texture colorAttachment;
    private Rbo depthStencilRbo;
    private MemoryContext memoryContext;

    private Fbo() {
        this.id = genFbo();
    }

    public void bind() {
        bind(id);
    }

    public void unbind() {
        unbind(id);
    }

    public void close() {
        GL30.glDeleteFramebuffers(id);
    }

    private void checkStatus() {
        if (GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER) == GL30.GL_FRAMEBUFFER_COMPLETE) {
            LOGGER.debug("Healthy fbo: {}", id);
        } else {
            throw new RuntimeException("Not completed framebufer.");
        }
    }

    private static int genFbo() {
        return GL30.glGenFramebuffers();
    }

    public static void bind(int id) {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, id);
    }

    public static void unbind(int id) {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }

    public Texture getColorAttachment() {
        return colorAttachment;
    }

    public void update(int width, int height) {
        bind();
        if (colorAttachment != null){
            colorAttachment.close();
            colorAttachment = Texture.emptyTexture(width, height, memoryContext);
            GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL30.GL_TEXTURE_2D, colorAttachment.getId(), 0);
        }
        if (depthStencilRbo != null) {
            depthStencilRbo.close();
            depthStencilRbo = new Rbo(width, height, GL30.GL_DEPTH24_STENCIL8);
            GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_STENCIL_ATTACHMENT, GL30.GL_RENDERBUFFER, depthStencilRbo.getId());
        }
        checkStatus();
        unbind();
    }


    public static class Builder {

        private final MemoryContext context;
        private Texture colorAttachment;
        private Rbo depthStencilRbo;

        public Builder(MemoryContext context) {
            this.context = context;
        }

        public Builder withColorAttachment(int width, int height) {
            colorAttachment = Texture.emptyTexture(width, height, context);
            return this;
        }

        public Builder withStencilDepthAttachment(int width, int height) {
            depthStencilRbo = new Rbo(width, height, GL30.GL_DEPTH24_STENCIL8);
            return this;
        }

        public Fbo build() {
            Fbo fbo = new Fbo();
            fbo.bind();
            if (colorAttachment != null) {
                GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL30.GL_TEXTURE_2D, colorAttachment.getId(), 0);
            }
            if (depthStencilRbo != null) {
                GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_STENCIL_ATTACHMENT, GL30.GL_RENDERBUFFER, depthStencilRbo.getId());
            }
            fbo.checkStatus();
            fbo.colorAttachment = colorAttachment;
            fbo.depthStencilRbo = depthStencilRbo;
            fbo.memoryContext = context;
            fbo.checkStatus();
            fbo.unbind();
            return fbo;
        }
    }
}
