package com.gluonhq.ignite.micronaut.view;

import javafx.scene.Parent;

public interface View<T extends Parent> {

    T getRoot();

//    default WindowBuilder show() {
//        return new WindowBuilder(View.this );
//    }
//
//    final static class WindowBuilder {
//
//        private final View<?> view;
//
//        private String title;
//        private Window owner;
//        private Modality modality;
//
//        WindowBuilder(View<?> view) {
//            this.view = view;
//        }
//
//        public WindowBuilder title(String title) {
//            this.title = title;
//            return this;
//        }
//
//        public WindowBuilder owner(Window owner) {
//            this.owner = owner;
//            return this;
//        }
//
//        public WindowBuilder modality(Modality modality) {
//            this.modality = modality;
//            return this;
//        }
//
//        public Stage asWindow() {
//            Stage stage = new Stage();
//            stage.setScene( new Scene( view.getRoot()) );
//            Optional.ofNullable(title).ifPresent(stage::setTitle);
//            Optional.ofNullable(owner).ifPresent(stage::initOwner);
//            Optional.ofNullable(modality).ifPresent(stage::initModality);
//            stage.setOnCloseRequest( e -> stage.getScene().setRoot( new Region())); // Untie view root when stage closes
//            return stage;
//        }
//
//        public Dialog<?> asDialog() {
//            Dialog<?> dlg = new Dialog<>();
//            Optional.ofNullable(title).ifPresent(dlg::setTitle);
//            Optional.ofNullable(owner).ifPresent(dlg::initOwner);
//            Optional.ofNullable(modality).ifPresent(dlg::initModality);
//            dlg.getDialogPane().setContent(view.getRoot());
//            //TODO buttons should be customizable
//            dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
//            return dlg;
//        }
//
//    }

}