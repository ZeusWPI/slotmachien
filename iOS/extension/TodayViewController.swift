//
//  TodayViewController.swift
//  extension
//
//  Created by Feliciaan De Palmenaer on 20/08/14.
//  Copyright (c) 2014 Feliciaan De Palmenaer. All rights reserved.
//

import Cocoa
import NotificationCenter

class TodayViewController: NSViewController, NCWidgetProviding {

    @IBOutlet var label: NSTextField?
    @IBOutlet var button: NSButton?

    var status = Status.error

    override var nibName: String! {
        return "TodayViewController"
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        Settings.sharedInstance.username = "felikaan"
    }

    func widgetPerformUpdateWithCompletionHandler(completionHandler: ((NCUpdateResult) -> Void)!) {
        // Update your data and prepare for a snapshot. Call completion handler when you are done
        // with NoData if nothing has changed or NewData if there is new data since the last
        // time we called you
        Settings.sharedInstance.doRequest("status", succes: setResponse, error: setError)
        completionHandler(.NewData)
    }

    @IBAction func toggleDoorStatus(button: AnyObject?) {
        println("Toggle: username: " + Settings.sharedInstance.username)
        self.status.next()
        Settings.sharedInstance.doRequest(status.asAction(), succes: setResponse, error: setError)
    }

    func setResponse(response: String) -> () {
        self.label?.stringValue = response
        var responseStatus = Status(status: response)
        responseStatus.next()
        self.button?.stringValue = responseStatus.asAction()
        self.status = Status(status: response)
        return
    }

    func setError(error: String) -> () {
        self.label?.stringValue = error
        self.status = .error
        self.button?.stringValue = "refresh"
        return
    }

}
