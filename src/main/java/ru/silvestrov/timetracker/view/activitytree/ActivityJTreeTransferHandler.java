package ru.silvestrov.timetracker.view.activitytree;

import ru.silvestrov.timetracker.model.activitytree.ActivityTree;
import ru.silvestrov.timetracker.model.activitytree.ActivityTreeNode;

import javax.swing.*;
import javax.swing.tree.TreePath;
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
    private static final String MIME_TYPE = DataFlavor.javaJVMLocalObjectMimeType + "; class=" + ActivityTreeNode.class.getName();

    private ActivityTreeModel activityTreeModel;

    public ActivityJTreeTransferHandler(ActivityTreeModel activityTreeModel) {
        this.activityTreeModel = activityTreeModel;
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
            TreePath sourceSelectionPath = (TreePath) support.getTransferable().getTransferData(new DataFlavor(MIME_TYPE));
            Object sourceParent = sourceSelectionPath.getParentPath().getLastPathComponent();
            int oldIndex = activityTreeModel.getIndexOfChild(sourceParent, sourceSelectionPath.getLastPathComponent());

            ActivityTreeNode newChild = (ActivityTreeNode)sourceSelectionPath.getLastPathComponent();
            JTree.DropLocation dl = (JTree.DropLocation) support.getDropLocation();
            ActivityTree parent = (ActivityTree) dl.getPath().getLastPathComponent();

            parent.addChild(newChild);

            activityTreeModel.treeNodeRemoved(sourceSelectionPath.getParentPath(), newChild, oldIndex);
            activityTreeModel.treeNodeInserted(dl.getPath(), newChild);

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
        final TreePath sourceSelectionPath = tree.getSelectionPath();
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
                return sourceSelectionPath;
            }
        };
    }
}
