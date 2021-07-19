package entity.pathfinding;

import com.google.inject.Inject;
import common.Coordinates;
import entity.pathfinding.edge.AbstractEdge;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RelativePath implements Callable<Void> {

  public Coordinates target;
  public RelativePathNode finalPathNode = null;
  Coordinates source;
  @Inject EdgeStore edgeStore;
  Set<RelativePathNode> unvisitedPathNodeSet = new HashSet<>();
  Set<RelativePathNode> visitedPathNodeSet = new HashSet<>();

  static ExecutorService executor = Executors.newFixedThreadPool(4);
  Future future;

  public RelativePath(EdgeStore edgeStore, Coordinates source, Coordinates target) {
    this.source = source;
    this.target = target;
    this.edgeStore = edgeStore;
  }

  public void backgroundSearch() {
    future = executor.submit(this);
  }

  public boolean isSearching() {
    return this.future != null && !this.future.isDone();
  }

  public void search() throws Exception {
    for (AbstractEdge edge : this.edgeStore.getEdgeList()) {
      if (edge.isAvailable(new PathGameStoreOverride(), source)) {
        unvisitedPathNodeSet.add(
            new RelativePathNode(
                edge, source, target, new PathGameStoreOverride(), edge.getCost()));
      }
    }
    while (unvisitedPathNodeSet.size() > 0) {
      RelativePathNode current =
          unvisitedPathNodeSet.stream()
              .min(Comparator.comparingDouble(RelativePathNode::getCost))
              .get();

      if (current.getHeuristicCost() < 0.9) {
        System.out.println("found " + current.getHeuristicCost());
        finalPathNode = current;
        return;
      }

      this.unvisitedPathNodeSet.remove(current);
      this.visitedPathNodeSet.add(current);

      for (AbstractEdge edge : this.edgeStore.getEdgeList()) {
        if (edge.isAvailable(current.pathGameStoreOverride, current.getEndPosition())) {
          RelativePathNode newNode =
              new RelativePathNode(
                  edge,
                  current.getEndPosition(),
                  target,
                  current.pathGameStoreOverride,
                  current.getCostFromStart() + edge.getCost());

          if (this.visitedPathNodeSet.contains(newNode)
              || this.unvisitedPathNodeSet.contains(newNode)) continue;

          newNode.setPrevious(current);
          unvisitedPathNodeSet.add(newNode);
        }
      }
    }
    throw new Exception("no path found.");
  }

  public List<RelativePathNode> getPathEdgeList() {
    List<RelativePathNode> edgeList = new LinkedList<>();
    RelativePathNode current = finalPathNode;
    while (current != null) {
      edgeList.add(current);
      current = current.getPrevious();
    }
    Collections.reverse(edgeList);
    return edgeList;
  }

  @Override
  public Void call() throws Exception {
    this.search();
    return null;
  }
}
