package animation;

public class AnimatorValue {
	private long timeDuration;
	private Thread thread;
	private AnimatorUpdate animatorUpdate;

	public AnimatorValue(long timeDuration) {
		this.timeDuration = timeDuration;
	}

	public void start() {
		if (thread != null) {
			thread.interrupt();
		}
		long startTime = System.currentTimeMillis();
		long endTime = startTime + timeDuration;
		thread = new Thread(new Runnable() {
			public void run() {
				if (animatorUpdate != null) {
					while (true) {
						if (System.currentTimeMillis() >= endTime) {
							animatorUpdate.updateAnimation(1);
							animatorUpdate.endAnimation();
							break;
						} else {
							animatorUpdate
									.updateAnimation((float) (System.currentTimeMillis() - startTime) / (float) (endTime - startTime));
						}
					}
				}
			}
		});
		thread.start();
	}

	public void setAnimatorUpdate(AnimatorUpdate animatorUpdate) {
		this.animatorUpdate = animatorUpdate;
	}

	public interface AnimatorUpdate {
		void updateAnimation(float fraction);
		void endAnimation();
	}
}
