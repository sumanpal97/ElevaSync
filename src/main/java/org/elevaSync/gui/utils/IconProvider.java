package org.elevaSync.gui.utils;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.elevaSync.System.utilities.ElevatorStatus;
import org.elevaSync.System.utilities.MoveDirection;

public class IconProvider {
    public ImageView getElevatorIcon(MoveDirection direction, ElevatorStatus status) {
        Image icon;
        if (direction == MoveDirection.STOP) {
            icon = new Image("icons/stop.png");
        } else if (status == ElevatorStatus.OUT_OF_SERVICE) {
            icon = new Image("icons/oosl.png");
        } else if (direction == MoveDirection.IDLE) {
            icon = new Image("icons/idle.png");
        } else {
            icon = new Image("icons/moving.png");
        }

        ImageView imageView = new ImageView(icon);
        imageView.setFitWidth(32);
        imageView.setFitHeight(32);

        return imageView;
    }
}
