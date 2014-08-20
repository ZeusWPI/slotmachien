//
//  ViewController.swift
//  slotmachien.osx
//
//  Created by Feliciaan De Palmenaer on 20/08/14.
//  Copyright (c) 2014 Feliciaan De Palmenaer. All rights reserved.
//

import Cocoa
import AppKit

class OSXViewController: NSViewController {

    @IBOutlet var openButton: NSButton?
    @IBOutlet var closeButton: NSButton?
    @IBOutlet var refreshButton: NSButton?

    @IBOutlet var textView: NSTextField?
    @IBOutlet var label: NSTextField?
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        if let s = self.label {
            s.stringValue = "Loading..."
        }

        // Request door status
        changeStatusDoor("status")

        // Set username
        let username = Settings.sharedInstance.username
        if countElements(username) > 0 {
            self.textView?.stringValue = username
        }

    }

    override var representedObject: AnyObject? {
        didSet {
        // Update the view, if already loaded.
        }
                                    
    }

    func changeStatusDoor(action: String) {
        Settings.sharedInstance.doRequest(action, succes: { (response) -> () in
            self.label?.stringValue = response
            return
            }) { (error) -> () in
                self.label?.stringValue = error
                return
        }
    }

    @IBAction func openDoor(button: AnyObject?) {
        changeStatusDoor("open")
    }

    @IBAction func closeDoor(button: AnyObject?) {
        changeStatusDoor("close")
    }

    @IBAction func refreshStatus(button: AnyObject?) {
        changeStatusDoor("status")
    }

    @IBAction func usernameFieldEndEditing(textField: NSTextField?) {

        if let tf = textField {
            let text = tf.stringValue
            println("new username: " + text)
            Settings.sharedInstance.username = text
            refreshStatus(nil)
        }
    }
}

