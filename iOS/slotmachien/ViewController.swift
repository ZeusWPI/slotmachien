//
//  ViewController.swift
//  slotmachien
//
//  Created by Feliciaan De Palmenaer on 15/08/14.
//  Copyright (c) 2014 Feliciaan De Palmenaer. All rights reserved.
//

import UIKit

class ViewController: UIViewController, UITextFieldDelegate {
    
    @IBOutlet var openButton: UIButton?
    @IBOutlet var closeButton: UIButton?
    
    @IBOutlet var statusLabel: UILabel?
    
    @IBOutlet var usernameTextField: UITextField?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib. 
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        
        if let s = self.statusLabel {
            s.text = "Loading..."
        }
        
        // Request door status
        changeStatusDoor("status")
        
        // Set username
        let username = Settings.sharedInstance.username
        if countElements(username) > 0 {
            self.usernameTextField?.text = username
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    
    func changeStatusDoor(action: String) {
        Settings.sharedInstance.doRequest(action, succes: { (response) -> () in
            self.statusLabel?.text = response
            return
        }) { (error) -> () in
            self.statusLabel?.text = error
            return
        }
    }
    
    @IBAction func openDoor() {
        changeStatusDoor("open")
    }
    
    @IBAction func closeDoor() {
        changeStatusDoor("close")
    }
    
    @IBAction func refreshStatus() {
        changeStatusDoor("status")
    }
    
    @IBAction func usernameFieldEndEditing(textField: UITextField!) {
        println("new username: " + textField.text)
        Settings.sharedInstance.username = textField.text
        refreshStatus()
    }
    
}

