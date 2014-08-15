//
//  TodayViewController.swift
//  todayExtension
//
//  Created by Feliciaan De Palmenaer on 15/08/14.
//  Copyright (c) 2014 Feliciaan De Palmenaer. All rights reserved.
//

import UIKit
import NotificationCenter

class TodayViewController: UIViewController {
    
    @IBOutlet var label: UILabel?
    @IBOutlet var toggleButton: UIButton?
    
    var status = Status.error
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view from its nib.
        Settings.sharedInstance.username = "felikaan" // temporary fix //TODO: fix it
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        Settings.sharedInstance.doRequest("status", succes: { (response) -> () in
            self.label?.text = response
            var responseStatus = Status(status: response)
            responseStatus.next()
            self.toggleButton?.setTitle(responseStatus.asAction(), forState: UIControlState.Normal)
            self.status = Status(status: response)
            return
        }) { (error) -> () in
            self.label?.text = error
            self.status = .error
            return
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func widgetPerformUpdateWithCompletionHandler(completionHandler: ((NCUpdateResult) -> Void)!) {
        // Perform any setup necessary in order to update the view.

        // If an error is encoutered, use NCUpdateResult.Failed
        // If there's no update required, use NCUpdateResult.NoData
        // If there's an update, use NCUpdateResult.NewData

        completionHandler(NCUpdateResult.NewData)
    }
    
    @IBAction func toggleDoorStatus() {
        self.status.next()
        Settings.sharedInstance.doRequest(status.asAction(), succes: { (response) -> () in
            self.label?.text = response
            var responseStatus = Status(status: response)
            responseStatus.next()
            self.toggleButton?.setTitle(responseStatus.asAction(), forState: UIControlState.Normal)
            self.status = Status(status: response)
            return
        }) { (error) -> () in
            self.label?.text = error
            self.status = .error
            self.toggleButton?.setTitle("refresh", forState: UIControlState.Normal)
            return
        }
    }
    
}
