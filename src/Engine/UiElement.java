package Engine;


/**
 * Basic UI interface for which UI shares
 */
public interface UiElement {

  /**
   * Method for creating (or revealing) a scene (editor, shop, ect)
   */
  public void create();

  /**
   * Method for destroying (or removing) a scene (editor, shop, ect)
   */
  public void destroy();

}