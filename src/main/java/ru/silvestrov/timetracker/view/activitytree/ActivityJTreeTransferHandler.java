package ru.silvestrov.timetracker.view.activitytree;

import ru.silvestrov.timetracker.model.activitytree.LazyActivityTreeNode;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * Created by Silvestrov Ilya
 * Date: 7/24/12
 * Time: 10:48 PM
 */
public class ActivityJTreeTransferHandler extends TransferHandler {
    private static final String MIME_TYPE = DataFlavor.javaJVMLocalObjectMimeType + "; class=" + LazyActivityTreeNode.class.getName();

    private ActivityTreeModel activityTreeModel;
    private ActivityJTree activityJTree;

    public ActivityJTreeTransferHandler(ActivityTreeModel activityTreeModel, ActivityJTree activityJTree) {
        this.activityTreeModel = activityTreeModel;
        this.activityJTree = activityJTree;
    }

    @Override
    public boolean canImport(TransferSupport support) {
        for (DataFlavor flavor : support.getDataFlavors()) {
            if (flavor.getMimeType().equals(MIME_TYPE)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean importData(TransferSupport support) {
        try {
            LazyActivityTreeNode newChild = (LazyActivityTreeNode) support.getTransferable().getTransferData(new DataFlavor(MIME_TYPE));
            JTree.DropLocation dl = (JTree.DropLocation) support.getDropLocation();
            LazyActivityTreeNode parent = (LazyActivityTreeNode) dl.getPath().getLastPathComponent();
            parent.addChild(newChild);
            activityTreeModel.treeNodeInserted(dl.getPath(), newChild);
            activityJTree.revalidate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getSourceActions(JComponent c) {
        return MOVE;
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        JTree tree = (JTree) c;
        final LazyActivityTreeNode node = (LazyActivityTreeNode) tree.getSelectionPath().getLastPathComponent();
        return new Transferable() {
            @Override
            public DataFlavor[] getTransferDataFlavors() {
                try {
                    return new DataFlavor[] {new DataFlavor(MIME_TYPE)};
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }

            @Override
            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return true;
            }

            @Override
            public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                return node;
            }
        };
    }
}
