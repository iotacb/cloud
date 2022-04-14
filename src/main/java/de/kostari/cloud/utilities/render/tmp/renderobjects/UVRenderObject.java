package de.kostari.cloud.utilities.render.tmp.renderobjects;

import de.kostari.cloud.utilities.color.CColor;
import de.kostari.cloud.utilities.math.CMath;
import de.kostari.cloud.utilities.math.Vec;
import de.kostari.cloud.utilities.render.Texture;
import de.kostari.cloud.utilities.render.tmp.RenderObject;

public class UVRenderObject extends RenderObject {

        private float regWidth;
        private float regHeight;
        private float regX;
        private float regY;

        private Texture texture;

        public UVRenderObject(Texture texture, float x, float y, float regX, float regY, float regWidth,
                        float regHeight, float rotation, CColor color) {
                super(x, y, regWidth, regHeight, rotation, color, false);

                this.regWidth = regWidth;
                this.regHeight = regHeight;
                this.regX = regX;
                this.regY = regY;
                this.texture = texture;
        }

        @Override
        public Vec[][] getVerticesUV() {
                Vec[][] vertices = new Vec[4][2];

                float x2 = x + regWidth;
                float y2 = y + regHeight;

                float s1 = regX / texture.getWidth();
                float t1 = regY / texture.getHeight();
                float s2 = (regX + regWidth) / texture.getWidth();
                float t2 = (regY + regHeight) / texture.getHeight();

                vertices = new Vec[][] {
                                new Vec[] {
                                                new Vec(x, y2),
                                                new Vec(s1, t1),
                                },
                                new Vec[] {
                                                new Vec(x2, y2),
                                                new Vec(s2, t1),
                                },
                                new Vec[] {
                                                new Vec(x2, y),
                                                new Vec(s2, t2),
                                },
                                new Vec[] {
                                                new Vec(x, y),
                                                new Vec(s1, t2),
                                }
                };

                for (int i = 0; i < vertices.length; i++) {
                        CMath.rotate(vertices[i][0], new Vec(x, y), rotation);
                }

                return vertices;
        }

}
