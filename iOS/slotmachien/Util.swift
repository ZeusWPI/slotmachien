//
//  Settings.swift
//  slotmachien
//
//  Created by Feliciaan De Palmenaer on 15/08/14.
//  Copyright (c) 2014 Feliciaan De Palmenaer. All rights reserved.
//

import Foundation

private let _SettingsSharedInstance = Settings()
private let _defaultsUsername = "SMUserName"

class Settings {

    class var sharedInstance : Settings {
    return _SettingsSharedInstance
    }

    let url: String
    let token: String
    var username: String {
        get {
            let u:String? = defaults.objectForKey(_defaultsUsername) as String?
            if let un = u {
                return un
            }
            return ""
        }
        set(newUsername) {
            defaults.setValue(newUsername, forKey: _defaultsUsername)
            defaults.synchronize()
        }
    }

    let defaults = NSUserDefaults(suiteName: "group.be.ugent.zeus")

    init() {
        var path = NSBundle.mainBundle().pathForResource("slotmachien", ofType: "plist")
        var dict = NSDictionary(contentsOfFile: path!)
        self.url = dict["url"] as String
        self.token = dict["token"] as String
    }

    func doRequest(action: String, succes: (response: String) -> (), error: (error: String) -> ()) {
        let options = ["token": token, "user_name": username, "action": action]
        Alamofire.request(Alamofire.Method.POST, self.url, parameters:options, encoding: Alamofire.ParameterEncoding.JSON(nil))
            .responseJSON { (request, response, JSON, requestError) in
                if requestError == nil && response?.statusCode == 200 {
                    if let jsonDict = JSON as? NSDictionary {
                        if let status = jsonDict.objectForKey("status") as? String {
                            succes(response: status)
                            return
                        }
                    }
                } else if response?.statusCode == 401 {
                    error(error: "An authorization error occured")
                    return
                }
                error(error: "An error occured during the request")
        }
    }
}

enum Status {
    case closed, open, error

    // create enum from string
    init(status:String) {
        switch status {
        case "open":
            self = .open
        case "closed", "close":
            self = .closed
        default:
            self = .error
        }
    }

    func asAction() -> String {
        switch self {
        case .open:
            return "open"
        case .closed:
            return "close"
        case .error:
            return "status"
        }
    }

    mutating func next() {
        switch self {
        case .open:
            self = .closed
        case .closed:
            self = .open
        default:
            self = .error
        }
    }
}
