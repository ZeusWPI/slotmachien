//
//  AppDelegate.swift
//  slotmachien.osx
//
//  Created by Feliciaan De Palmenaer on 20/08/14.
//  Copyright (c) 2014 Feliciaan De Palmenaer. All rights reserved.
//

import Cocoa

class AppDelegate: NSObject, NSApplicationDelegate {

    @IBOutlet var viewController: OSXViewController?

    func applicationDidFinishLaunching(aNotification: NSNotification?) {
        // Insert code here to initialize your application
        self.viewController = OSXViewController()

    }

    func applicationWillTerminate(aNotification: NSNotification?) {
        // Insert code here to tear down your application
    }


}

