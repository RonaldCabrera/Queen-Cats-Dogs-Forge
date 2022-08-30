package net.pevori.queencats.entity.goals;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.pevori.queencats.entity.custom.HumanoidDogEntity;

public class HumanoidDogMeleeGoal extends Goal {
    private final HumanoidDogEntity entity;
    public int attackTime;
    private double moveSpeedAmp = 1;

	public HumanoidDogMeleeGoal(HumanoidDogEntity entity, double speed) {
		this.entity = entity;
		this.moveSpeedAmp = speed;
	}

    @Override
	public boolean canStart() {
		return this.entity.getTarget() != null;
	}

    public boolean shouldContinue() {
		return this.canStart();
	}

	public void start() {
		super.start();
		this.entity.setAttacking(true);
	}

	public void stop() {
		super.stop();
		this.entity.setAttacking(false);
		this.entity.setAttackingState(false);
		this.attackTime = -1;
	}

	public void tick() {
		LivingEntity livingentity = this.entity.getTarget();
		if (livingentity != null && !entity.isSitting()) {
			boolean inLineOfSight = this.entity.getVisibilityCache().canSee(livingentity);
			this.attackTime++;
			this.entity.lookAtEntity(livingentity, 30.0F, 30.0F);
			double d0 = this.entity.squaredDistanceTo(livingentity.getX(), livingentity.getY(),
					livingentity.getZ());
			double d1 = this.getAttackReachSqr(livingentity);
			if (inLineOfSight) {
				if (this.entity.distanceTo(livingentity) >= 3.0D) {
					this.entity.getNavigation().startMovingTo(livingentity, this.moveSpeedAmp);
					this.attackTime = -5;
				} else {
					if (this.attackTime == 2) {
						this.entity.getNavigation().startMovingTo(livingentity, this.moveSpeedAmp);
						if (d0 <= d1) {
							this.entity.tryAttack(livingentity);
							this.entity.setAttackingState(true);
						}
						livingentity.timeUntilRegen = 0;
					}
					if (this.attackTime == 8) {
						this.attackTime = -5;
						this.entity.setAttackingState(false);
					}
				}
			}
		}
	}

	protected double getAttackReachSqr(LivingEntity entity) {
		return (double) (this.entity.getWidth() * 1.5F * this.entity.getWidth() * 1.5F + entity.getWidth());
	}
    
}
