package dev.xkmc.l2hostility.content.capability.chunk;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public class ChunkClearRenderer {

	public static void render(PoseStack pose, Player player, ChunkDifficulty center) {
		int r = 7;
		boolean[][][] sections = new boolean[r * 2 + 1][r * 2 + 1][r * 2 + 1];
		int cx = center.chunk.getPos().x;
		int cz = center.chunk.getPos().z;
		int py = Mth.floor((float) player.getEyeY());
		for (int i = 0; i < r * 2 + 1; i++) {
			for (int j = 0; j < r * 2 + 1; j++) {
				int ix = i - r + cx;
				int iz = j - r + cz;
				var ic = ChunkDifficulty.at(player.level(), ix, iz).orElse(null);
				for (int k = 0; k < r * 2 + 1; k++) {
					sections[i][j][k] = ic.getSection(py + (k - r) * 16).isCleared();
				}
			}
		}
		pose.pushPose();
		pose.translate(-player.getX(), -player.getEyeY(), -player.getZ());
		new ChunkClearRenderer(pose, r, sections, cx, cz, py >> 4);
		pose.popPose();
	}

	private final int r, d, greenCol, redCol, lineCol;
	private final long cx, cz, cy;
	private final boolean[][][] sections;
	private final boolean inClear;
	private final PoseStack pose;
	private VertexConsumer cons;

	private ChunkClearRenderer(PoseStack pose, int r, boolean[][][] sections, long cx, long cz, long cy) {
		this.pose = pose;
		this.r = r;
		this.d = r * 2 + 1;
		this.sections = sections;
		this.cx = cx - r;
		this.cz = cz - r;
		this.cy = cy - r;
		this.inClear = sections[r][r][r];
		this.lineCol = 0xffffffff;
		this.greenCol = 0x1f00ff00;
		this.redCol = 0x1fff0000;
		render();
	}

	private void render() {
		var source = Minecraft.getInstance().renderBuffers().bufferSource();
		this.cons = source.getBuffer(RenderType.debugQuads());
		for (int x = -1; x <= d; x++) {
			for (int z = -1; z <= d; z++) {
				for (int y = -1; y <= d; y++) {
					boolean self = get(x, z, y);
					boolean nx = get(x + 1, z, y);
					boolean nz = get(x, z + 1, y);
					boolean ny = get(x, z, y + 1);
					if (self ^ nx) {
						vertex(x + 1, y, z);
						vertex(x + 1, y + 1, z);
						vertex(x + 1, y + 1, z + 1);
						vertex(x + 1, y, z + 1);
					}
					if (self ^ ny) {
						vertex(x, y + 1, z);
						vertex(x + 1, y + 1, z);
						vertex(x + 1, y + 1, z + 1);
						vertex(x, y + 1, z + 1);
					}
					if (self ^ nz) {
						vertex(x, y, z + 1);
						vertex(x + 1, y, z + 1);
						vertex(x + 1, y + 1, z + 1);
						vertex(x, y + 1, z + 1);
					}
				}
			}
		}
		this.cons = source.getBuffer(RenderType.debugLineStrip(10));
		Edge ex = new Edge(1, 0, 0);
		Edge ey = new Edge(0, 1, 0);
		Edge ez = new Edge(0, 0, 1);
		for (int x = -1; x <= d; x++) {
			for (int z = -1; z <= d; z++) {
				for (int y = -1; y <= d; y++) {
					ex.drawEdge(x, y, z);
					ey.drawEdge(x, y, z);
					ez.drawEdge(x, y, z);
				}
			}
		}
		source.endLastBatch();
	}

	private void vertex(int x, int y, int z) {
		int c = inClear ? redCol : greenCol;
		var mat = pose.last().pose();
		cons.vertex(mat, (cx + x) << 4, (cy + y) << 4, (cz + z) << 4)
				.color(c).endVertex();
	}

	private boolean get(int x, int z, int y) {
		if (x < 0 || x >= d) return false;
		if (z < 0 || z >= d) return false;
		if (y < 0 || y >= d) return false;
		return sections[x][z][y];
	}

	private class Edge {

		private final int dx, dy, dz, ax, ay, az, bx, by, bz;

		private Edge(int x, int y, int z) {
			dx = x;
			dy = y;
			dz = z;
			ax = 1 - dx;
			ay = 1 - ax;
			az = 0;
			bx = 1 - dx - ax;
			by = 1 - dy - ay;
			bz = 1 - dz - az;
		}

		private void drawEdge(int x, int y, int z) {
			boolean s = get(x, z, y);
			boolean a = get(x + ax, z + az, y + ay);
			boolean b = get(x + bx, z + bz, y + by);
			boolean ab = get(x + ax + bx, z + az + bz, y + ay + by);
			if (!s && !a && !b && !ab) return;
			int fa = 0, fb = 0;
			if (s) {
				fa++;
				fb++;
			}
			if (a) {
				fa--;
				fb++;
			}
			if (b) {
				fa++;
				fb--;
			}
			if (ab) {
				fa--;
				fb--;
			}
			vertex(x, y, z, fa, fb);
		}

		private void vertex(int x, int y, int z, float fa, float fb) {
			float df = inClear ? -0.003f : 0.003f;
			float fx = df * (fa * ax + fb * bx);
			float fy = df * (fa * ay + fb * by);
			float fz = df * (fa * az + fb * bz);
			long x0 = (cx + x + 1 - dx) << 4;
			long y0 = (cy + y + 1 - dy) << 4;
			long z0 = (cz + z + 1 - dz) << 4;
			long x1 = (cx + x + 1) << 4;
			long y1 = (cy + y + 1) << 4;
			long z1 = (cz + z + 1) << 4;

			vertex(x0 + fx, y0 + fy, z0 + fz, 0);
			vertex(x0 + fx, y0 + fy, z0 + fz, lineCol);
			vertex(x1 + fx, y1 + fy, z1 + fz, lineCol);
			vertex(x1 + fx, y1 + fy, z1 + fz, 0);
		}


		private void vertex(float x, float y, float z, int c) {
			cons.vertex(pose.last().pose(), x, y, z).color(c).endVertex();
		}


	}

}
