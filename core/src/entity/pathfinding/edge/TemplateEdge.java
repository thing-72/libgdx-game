package entity.pathfinding.edge;

import common.Coordinates;
import entity.Entity;
import entity.pathfinding.*;

import java.util.List;

public class TemplateEdge extends AbstractEdge {

  List<RelativeActionEdge> actionEdgeList;

  int currentStep = 0;

  public TemplateEdge(
      EntityStructure entityStructure,
      RelativeVertex from,
      RelativeVertex to,
      List<RelativeActionEdge> actionEdgeList) {
    super(entityStructure, from, to);
    this.actionEdgeList = actionEdgeList;
  }

  @Override
  public double getCost() {
    return 1;
  }

  public List<RelativeActionEdge> getActionEdgeList() {
    return actionEdgeList;
  }

  @Override
  public boolean isAvailable(PathGameStoreOverride pathGameStoreOverride, Coordinates coordinates) {
    if (!coordinates.equals(coordinates.getBase())) {
      return false;
    }
    return super.isAvailable(pathGameStoreOverride, coordinates);
  }

  @Override
  public void finish() {
    super.finish();
    this.currentStep = 0;
  }

  public void registerActionEdge(RelativeActionEdge actionEdge) {
    this.actionEdgeList.add(actionEdge);
  }

  public RelativeActionEdge getLastEdge() {
    return this.actionEdgeList.get(this.actionEdgeList.size() - 1);
  }

  @Override
  public EdgeStepper getEdgeStepper(Entity entity, RelativePathNode relativePathNode) {
    return new TemplateEdgeStepper(this.actionEdgeList);
  }
}

class TemplateEdgeStepper extends EdgeStepper {

  List<RelativeActionEdge> actionEdgeList;
  int currentStep = 0;

  public TemplateEdgeStepper(List<RelativeActionEdge> actionEdgeList) {
    this.actionEdgeList = actionEdgeList;
  }

  @Override
  public void follow(Entity entity, RelativePathNode relativePathNode) throws Exception {
    RelativeActionEdge currentEdge = this.actionEdgeList.get(currentStep);
    currentStep++;
    String actionKey = currentEdge.actionKey;
    entity.entityController.applyAction(actionKey, entity.getBody());
    if (currentStep == this.actionEdgeList.size()) this.finish();
  }
}