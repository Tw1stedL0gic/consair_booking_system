/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ospp.booking.bookingfx;

/**
 * @author Andreas
 */
public interface ControlledScreen {

	public void setScreenParent(ScreenMaster sm);

	public void onScreen();

	public void offScreen();
}
